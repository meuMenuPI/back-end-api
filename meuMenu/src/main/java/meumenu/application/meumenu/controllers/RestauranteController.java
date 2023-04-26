package meumenu.application.meumenu.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import meumenu.application.meumenu.assossiativas.FavoritoRepository;
import meumenu.application.meumenu.restaurante.*;
import meumenu.application.meumenu.usuario.Usuario;
import meumenu.application.meumenu.usuario.UsuarioDTO;
import meumenu.application.meumenu.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Tag(name = "Documentação dos end-points de restaurantes", description = "Documentação viva dos restaurantes feita via swagger")
@RestController
@RequestMapping("/meumenu/restaurantes")
public class RestauranteController {
    @Autowired
    private RestauranteRepository repository;

    @Autowired
    private UsuarioRepository repositoryUsuario;

    @Autowired
    private FavoritoRepository repositoryFavorito;

    // biblioteca responsavel por mandar o email
    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/cadastrar")
    @Operation(summary = "Metodo de cadastrar restaurante", description = "Cadastra restaurante", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante cadastrado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante cadastrado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<RestauranteDTO> cadastrar(@RequestBody @Valid DadosCadastroRestaurante dados) {
        repository.save(new Restaurante(dados));
        List<Restaurante> tempRestaurante = repository.findAll();
        RestauranteDTO restaurante = new RestauranteDTO(tempRestaurante.get(tempRestaurante.size() - 1).getId(), dados.nome(), dados.especialidade().name(), dados.telefone(), dados.site(), dados.estrela());
        return ResponseEntity.status(200).body(restaurante);
    }

    @GetMapping
    @Operation(summary = "Metodo de listar todos os restaurantes", description = "Lista todos restaurantes", responses = {@ApiResponse(responseCode = "200", description = "Sucesso lista retornada!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada!\"}"),})), @ApiResponse(responseCode = "204", description = "Sucesso lista retornada mas vazia!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 204', \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada mas vazia!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<List<RestauranteDTO>> listar() {
        List<RestauranteDTO> restauranteDTO = new ArrayList<>();
        List<Restaurante> tempRestaurante = repository.findAll();
        for (Restaurante r : tempRestaurante) {
            restauranteDTO.add(new RestauranteDTO(r.getId(), r.getNome(), r.getEspecialidade().name(), r.getTelefone(), r.getSite(), r.getEstrela()));
        }
        return ResponseEntity.status(200).body(restauranteDTO);
    }

    @GetMapping("{id}")
    @Operation(summary = "Metodo de listar restaurante por id", description = "Lista restaurante por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante listado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante listado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<RestauranteDTO> listarPorId(@PathVariable Integer id) {
        Optional<Restaurante> r = repository.findById(id);
        if(r.isPresent()){
            RestauranteDTO restaurante = new RestauranteDTO(r.get().getId(),r.get().getNome(), r.get().getEspecialidade().name(), r.get().getTelefone(), r.get().getSite(), r.get().getEstrela());
            return ResponseEntity.status(200).body(restaurante);
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Metodo de atualizar dados do restaurante", description = "Atualiza restaurante por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante atualizado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante atualizado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<String> atualizar(@RequestBody @Valid Restaurante dados, @PathVariable int id) {
        if (repository.existsById(id)) {
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
            return ResponseEntity.status(200).body("Restaurante atualizado com sucesso");
        }
        return ResponseEntity.status(404).body("Restaurante não encontrado");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Metodo de deletar restaurante por id", description = "Deleta restaurante por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante deletado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante deletado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<String> deletar(@PathVariable int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.status(200).body("Restaurante deletado com sucesso");
        }
        return ResponseEntity.status(404).body("Restaurante não encontrado");
    }

    @GetMapping("/recomendar/{id}")
    @Transactional
    public ResponseEntity<List<UsuarioDTO>> recomendar(@PathVariable int id) {

        List<Restaurante> listaRestaurante = repository.findAll();
        List<Usuario> listaUsuario = repositoryUsuario.findAll();
        List<UsuarioDTO> usuarioDTO = new ArrayList<>();

        if (!repository.existsById(id)) {
            return ResponseEntity.status(404).build();
        }

        for (Restaurante r : listaRestaurante) {
            if (r.getId() == id) {
                usuarioDTO = (r.recomendar(listaUsuario, listaRestaurante, id));
            }
        }
        if (usuarioDTO.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(usuarioDTO);
    }

    @PostMapping("email/{id}")
    @Operation(summary = "Metodo de enviar email para usuarios", description = "Envia email para usuarios", responses = {@ApiResponse(responseCode = "200", description = "Sucesso email enviado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso email enviado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<String[]> enviarEmail(@RequestBody Email email, @PathVariable int id) {
        String vetor[] = repositoryFavorito.findAllFavoritos(id);
        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom("meumenu.contato@gmail.com");
            mailMessage.setText(email.getTexto());
            mailMessage.setSubject(email.getTitulo());

            for (int i = 0; i < vetor.length; i++) {
                mailMessage.setTo(vetor[i]);
                javaMailSender.send(mailMessage);
            }
            return ResponseEntity.status(200).body(vetor);
        } catch (Exception erro) {
            return ResponseEntity.status(400).body(vetor);
        }
    }

    //GRAVAR ARQUIVO CSV ---------------
    @GetMapping("/download/{id}")
    @Operation(summary = "Metodo de baixar os dados dos usuarios correspondentes", description = "Download csv dos dados dos usuarios que tem o tipo de comida favorita igual ao restaurante", responses = {@ApiResponse(responseCode = "200", description = "Sucesso email enviado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso email enviado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    public ResponseEntity<String> gravaArquivoCsv(@PathVariable int id) {
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
                saida.format("%4d;%-8s;%-12s;%-10s;%-20s\n", usuariosDTO.get(i).getId(), usuariosDTO.get(i).getNome(), usuariosDTO.get(i).getSobrenome(), usuariosDTO.get(i).getEmail(), usuariosDTO.get(i).getTipoComidaFavorita());
            }
        } catch (FormatterClosedException e) {
            System.out.println("Erro na formatação do arquivo");
            deuRuim = true;
        } finally {
            saida.close();
            try {
                arq.close();
                return ResponseEntity.status(200).body("Download feito com sucesso");
            } catch (IOException e) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
                System.exit(1);
                return ResponseEntity.status(500).build();
            }
        }
    }
}


