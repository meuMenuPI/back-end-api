package meumenu.application.meumenu.services;

import meumenu.application.meumenu.favorito.FavoritoRepository;
import meumenu.application.meumenu.exceptions.CsvException;
import meumenu.application.meumenu.exceptions.EmailException;
import meumenu.application.meumenu.exceptions.NaoEncontradoException;
import meumenu.application.meumenu.restaurante.*;
import meumenu.application.meumenu.usuario.Usuario;
import meumenu.application.meumenu.usuario.UsuarioDTO;
import meumenu.application.meumenu.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
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

    public void cadastrar(DadosCadastroRestaurante dados) {
        this.repository.save(new Restaurante(dados));
    }

    public List<RestauranteDTO> listar() {
        List<RestauranteDTO> restauranteDTO = new ArrayList<>();
        List<Restaurante> tempRestaurante = repository.findAll();
        if (tempRestaurante.isEmpty()) throw new NaoEncontradoException("Nenhum restaurante encontrado");
        for (Restaurante r : tempRestaurante) {
            meumenu.application.meumenu.restaurante.RestauranteDTO dto = new RestauranteDTO(r.getId(), r.getNome(), r.getEspecialidade().name(), r.getTelefone(), r.getSite(), r.getEstrela());
            restauranteDTO.add(dto);
        }
        return restauranteDTO;
    }

    public RestauranteDTO listarPorId(Integer id) {
        Optional<Restaurante> r = repository.findById(id);
        if (r.isEmpty()) {
            throw new NaoEncontradoException("Nenhum restaurante encontrado");
        }
        RestauranteDTO restaurante = new RestauranteDTO(r.get().getId(), r.get().getNome(), r.get().getEspecialidade().name(), r.get().getTelefone(), r.get().getSite(), r.get().getEstrela());
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
        repository.save(restaurante);
        return restaurante;
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
                usuariosDTO.add(new UsuarioDTO(tempUsuario.get(i).getId(), tempUsuario.get(i).getNome(), tempUsuario.get(i).getSobrenome(), tempUsuario.get(i).getEmail(), tempUsuario.get(i).getTipoComidaPreferida().name()));
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
}

