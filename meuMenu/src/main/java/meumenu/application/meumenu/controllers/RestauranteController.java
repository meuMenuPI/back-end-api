package meumenu.application.meumenu.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import meumenu.application.meumenu.assossiativas.FavoritoRepository;
import meumenu.application.meumenu.restaurante.*;
import meumenu.application.meumenu.services.RestauranteService;
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
    @Autowired
    private RestauranteService service;

    // biblioteca responsavel por mandar o email
    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/cadastrar")
    @Operation(summary = "Metodo de cadastrar restaurante", description = "Cadastra restaurante", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante cadastrado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante cadastrado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<RestauranteDTO> cadastrar(@RequestBody @Valid DadosCadastroRestaurante dados) {
        this.service.cadastrar(dados);
        RestauranteDTO restaurante = new RestauranteDTO(dados.nome(), dados.especialidade().name(), dados.telefone(), dados.site(), dados.estrela());
        return ResponseEntity.created(null).body(restaurante);
    }

    @GetMapping
    @Operation(summary = "Metodo de listar todos os restaurantes", description = "Lista todos restaurantes", responses = {@ApiResponse(responseCode = "200", description = "Sucesso lista retornada!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada!\"}"),})), @ApiResponse(responseCode = "204", description = "Sucesso lista retornada mas vazia!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 204', \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada mas vazia!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<List<RestauranteDTO>> listar() {
        return ResponseEntity.ok(this.service.listar());
    }

    @GetMapping("{id}")
    @Operation(summary = "Metodo de listar restaurante por id", description = "Lista restaurante por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante listado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante listado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<RestauranteDTO> listarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(this.service.listarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Metodo de atualizar dados do restaurante", description = "Atualiza restaurante por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante atualizado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante atualizado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<String> atualizar(@RequestBody @Valid Restaurante dados, @PathVariable int id) {
        Restaurante restaurante = this.service.atualizar(dados, id);
        if (restaurante.equals(dados)) {
            return ResponseEntity.ok("Restaurante atualizado");
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Metodo de deletar restaurante por id", description = "Deleta restaurante por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante deletado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante deletado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        this.service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recomendar/{id}")
    @Transactional
    public ResponseEntity<List<UsuarioDTO>> recomendar(@PathVariable int id) {
        return ResponseEntity.ok(this.service.recomendar(id));
    }

    @PostMapping("email/{id}")
    @Operation(summary = "Metodo de enviar email para usuarios", description = "Envia email para usuarios", responses = {@ApiResponse(responseCode = "200", description = "Sucesso email enviado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso email enviado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<String[]> enviarEmail(@RequestBody Email email, @PathVariable Integer id) {
        String vetor [] = repositoryFavorito.findAllFavoritos(id);
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
        }
        catch (Exception erro) {
            return ResponseEntity.status(400).body(vetor);
        }
    }

    //GRAVAR ARQUIVO CSV ---------------
    @GetMapping("/download/{id}")
    @Operation(summary = "Metodo de baixar os dados dos usuarios correspondentes", description = "Download csv dos dados dos usuarios que tem o tipo de comida favorita igual ao restaurante", responses = {@ApiResponse(responseCode = "200", description = "Sucesso email enviado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso email enviado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    public ResponseEntity<String> gravaArquivoCsv(@PathVariable Integer id) {
        return ResponseEntity.ok(this.service.gravaArquivoCsv(id));
    }
}


