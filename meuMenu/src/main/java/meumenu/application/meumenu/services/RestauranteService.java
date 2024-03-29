package meumenu.application.meumenu.services;

import com.azure.core.http.rest.Response;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import meumenu.application.meumenu.endereco.DadosCadastroEndereco;
import meumenu.application.meumenu.endereco.Endereco;
import meumenu.application.meumenu.endereco.EnderecoRepository;
import meumenu.application.meumenu.favorito.FavoritoRepository;
import meumenu.application.meumenu.exceptions.CsvException;
import meumenu.application.meumenu.exceptions.EmailException;
import meumenu.application.meumenu.exceptions.NaoEncontradoException;
import meumenu.application.meumenu.restaurante.*;
import meumenu.application.meumenu.usuario.Usuario;
import meumenu.application.meumenu.usuario.UsuarioDTO;
import meumenu.application.meumenu.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RestauranteService {
    @Autowired
    private RestauranteRepository repository;
    @Autowired
    private UsuarioRepository repositoryUsuario;
    @Autowired
    private FavoritoRepository repositoryFavorito;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EnderecoRepository repositoryEndereco;

    @Autowired
    private RestauranteFotoRepository repositoryFoto;

    public DadosCadastroRestaurante cadastrar(DadosCadastroRestaurante dados) {
        this.repository.save(new Restaurante(dados));
        return dados;
    }

    public List<RestauranteDTO> listar() {
        List<RestauranteDTO> restauranteDTO = new ArrayList<>();
        List<Restaurante> tempRestaurante = repository.findAll();
        if (tempRestaurante.isEmpty()) throw new NaoEncontradoException("Nenhum restaurante encontrado");
        for (Restaurante r : tempRestaurante) {
            meumenu.application.meumenu.restaurante.RestauranteDTO dto = new RestauranteDTO(r.getId(), r.getNome(), r.getEspecialidade().name(), r.isBeneficio(), r.getTelefone(), r.getSite(), r.getEstrela());
            restauranteDTO.add(dto);
        }
        return restauranteDTO;
    }

    public RestauranteDTO listarPorId(Integer id) {
        Optional<Restaurante> r = repository.findById(id);
        if (r.isEmpty()) {
            throw new NaoEncontradoException("Nenhum restaurante encontrado");
        }
        RestauranteDTO restaurante = new RestauranteDTO(r.get().getId(), r.get().getNome(), r.get().getEspecialidade().name(), r.get().isBeneficio(), r.get().getTelefone(), r.get().getSite(), r.get().getEstrela());
        return restaurante;

    }

    public Restaurante atualizar(Restaurante dados, int id) {
        Restaurante restaurante = repository.findById(id).orElseThrow();
        if (dados.getNome() != null) {
            restaurante.setNome(dados.getNome());
        }
        if (dados.getSite() != null) {
            restaurante.setSite(dados.getSite());
        }
        if (dados.getTelefone() != null) {
            restaurante.setTelefone(dados.getTelefone());
        }
        if (dados.getEstrela() != null) {
            restaurante.setEstrela(dados.getEstrela());
        }
        if (dados.getEspecialidade() != null) {
            restaurante.setEspecialidade(dados.getEspecialidade());
        }
        repository.save(restaurante);
        return restaurante;
    }

    public Endereco atualizarEndereco(int id, Endereco dados) {
        Endereco endereco = repositoryEndereco.findById(id).orElseThrow();
        if (dados.getCep() != null) {
            endereco.setCep(dados.getCep());
        }
        if (dados.getNumero() != null) {
            endereco.setNumero(dados.getNumero());
        }
        if (dados.getComplemento() != null) {
            endereco.setComplemento(dados.getComplemento());
        }
        if (dados.getUf() != null) {
            endereco.setUf(dados.getUf());
        }
        repositoryEndereco.save(endereco);
        return endereco;
    }

    public void deletar(Integer id) {
        if (!repository.existsById(id)) {
            throw new NaoEncontradoException("Restaurante não encontrado");
        }
        repository.deleteById(id);
    }

    public List<UsuarioDTO> recomendar(Integer id) {
        List<Restaurante> listaRestaurante = repository.findAll();
        List<Usuario> listaUsuario = repositoryUsuario.findAll();
        List<UsuarioDTO> usuarioDTO = new ArrayList<>();

        if (!repository.existsById(id)) {
            throw new NaoEncontradoException("Nenhum restaurante encontrado");
        }

        for (Restaurante r : listaRestaurante) {
            if (r.getId() == id) {
                usuarioDTO = (r.recomendar(listaUsuario, listaRestaurante, id));
            }
        }
        if (usuarioDTO.isEmpty()) {
            throw new NaoEncontradoException("Nenhum usuario encontrado");
        }
        return usuarioDTO;
    }

    public String[] enviarEmail(Email email, Integer id) {
        String vetor[] = repositoryFavorito.findAllFavoritos(id);
        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom("meumenu.contato@gmail.com");
            mailMessage.setText(email.getTexto());
            mailMessage.setSubject(email.getTitulo());

            for (String s : vetor) {
                mailMessage.setTo(s);
                javaMailSender.send(mailMessage);
            }
            return vetor;
        } catch (Exception erro) {
            throw new EmailException("Não foi possivel enviar o email");
        }
    }

    public String gravaArquivoCsv(Integer id) {
        FileWriter arq = null;
        Formatter saida = null;
        Boolean deuRuim = false;

        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        List<Usuario> tempUsuario = repositoryUsuario.findAll();
        Optional<Restaurante> idRestaurante = repository.findById(id);
        for (int i = 0; i < tempUsuario.size(); i++) {
            if (tempUsuario.get(i).getTipoComidaPreferida().toString().equals(idRestaurante.get().getEspecialidade().toString())) {
                usuariosDTO.add(new UsuarioDTO(tempUsuario.get(i).getId(), tempUsuario.get(i).getNome(), tempUsuario.get(i).getSobrenome(), tempUsuario.get(i).getEmail(), tempUsuario.get(i).getTipoComidaPreferida().name(), tempUsuario.get(i).getFotoPerfil()));
            }
        }

        String nomeArq = System.getProperty("user.home") + "/Downloads/Usuarios_Gerais.csv";

        //Bloco try-catch para abri o arquivo
        try {
            arq = new FileWriter(nomeArq);
            saida = new Formatter(arq);

        } catch (IOException e) {
            System.out.println("Erro ao abrir o arquivo");
            e.printStackTrace();
            System.exit(1);
        }

        //Blobo try-catch para gravar o arquivo
        try {
            saida.format("%-4s;%-8s;%-12s;%-10s;%20s\n", "ID", "NOME", "SOBRENOME", "EMAIL", "TIPO COMIDA FAVORITA");
            for (int i = 0; i < usuariosDTO.size(); i++) {
                UsuarioDTO dog = usuariosDTO.get(i);
                saida.format("%4d;%-8s;%-12s;%-10s;%-20s\n", usuariosDTO.get(i).getId(), usuariosDTO.get(i).getNome(), usuariosDTO.get(i).getSobrenome(), usuariosDTO.get(i).getEmail(), usuariosDTO.get(i).getTipoComidaPreferida());
            }
        } catch (FormatterClosedException e) {
            System.out.println("Erro na formatação do arquivo");
            deuRuim = true;
        } finally {
            saida.close();
            try {
                arq.close();
                return "Download feito com sucesso";
            } catch (IOException e) {
                deuRuim = true;
                System.exit(1);
                throw new CsvException("Erro ao fazer download");
            }
        }
    }

    public void cadastrarEndereco(DadosCadastroEndereco dados) {

        this.repositoryEndereco.save(new Endereco(dados));
    }

    public void cadastrarImagem(int id, MultipartFile imagem) throws IOException {
        byte[] bytes = imagem.getBytes();
        if (bytes.length == 0) {
            throw new IOException("Imagem não contem bytes");
        }

        Optional<Restaurante> restaurante = repository.findById(id);

        if (restaurante.isEmpty()) {
            throw new RuntimeException("Restaurante não encontrado");
        }

        String fileName = LocalDateTime.now() + imagem.getOriginalFilename();

        String constr = "DefaultEndpointsProtocol=https;AccountName=meumenuimagens;AccountKey=R9lel0MHe6" +
                "BQTZj3c7dDQYXKKGiC75NpsmLi/IqBChb4NAGFT5kheiorbVyx/pSAo9VC5e/Ktkju+AStGIYs7w==;Endpoint" +
                "Suffix=core.windows.net";


        BlobContainerClient container = new BlobContainerClientBuilder()
                .connectionString(constr).containerName("restaurante").buildClient();

        BlobClient blob = container.getBlobClient(fileName);


        Response<BlockBlobItem> response = blob.uploadWithResponse(
                new BlobParallelUploadOptions(new ByteArrayInputStream(bytes), bytes.length),
                Duration.ofHours(5),
                null);

        if (response.getStatusCode() != 201) {
            throw new IOException("request failed");
        }

        int contador = repositoryFoto.countByFkRestaurante(id);

        RestauranteFoto restauranteFoto = new RestauranteFoto(id, fileName, false, false);

        if (contador == 0) {
            restauranteFoto.setFachada(true);
        } else if (contador == 1 || contador == 2) {
            restauranteFoto.setInterior(true);
        }

        this.repositoryFoto.save(restauranteFoto);
    }

    public List<RestauranteReviewDTO> listarReview() {
        List<RestauranteReviewDTO> restauranteDTO = repository.findByRestauranteBemAvaliadoDTO();
        return restauranteDTO;
    }

    public List<RestauranteReviewDTO> listarRestaurantePorUf(String uf) {
        List<RestauranteReviewDTO> restauranteDTO = repository.findByRestauranteUFDTO(uf);
        return restauranteDTO;
    }

    public List<String> listarEspecialidadesDistintas() {
        List<String> lista = repository.findByEspecialidadesDestintas();
        return lista;
    }

    public ResponseEntity<Endereco> listarEndereco(Integer id) {
        Optional<Endereco> endereco = repositoryEndereco.findByFkRestaurante(id);
        return ResponseEntity.of(endereco);
    }

    public Optional<Restaurante> pegarPelaFk(Integer fkUsuario){
        Optional <Restaurante> restaurante = repository.findByUsuario(fkUsuario);
        return restaurante;
    }
}

