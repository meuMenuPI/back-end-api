package meumenu.application.meumenu.controllers;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import meumenu.application.meumenu.assossiativas.Favorito;
import meumenu.application.meumenu.assossiativas.FavoritoId;
import meumenu.application.meumenu.assossiativas.FavoritoRepository;
import jdk.jfr.ContentType;
import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.restaurante.RestauranteDTO;
import meumenu.application.meumenu.restaurante.RestauranteRepository;
import meumenu.application.meumenu.usuario.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import meumenu.application.meumenu.usuario.DadosCadastroUsuario;
import meumenu.application.meumenu.usuario.Usuario;
import meumenu.application.meumenu.usuario.UsuarioRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Tag(name = "Documentação dos end-points de usuarios", description = "Documentação viva dos usuarios feita via swagger")
@RestController
@RequestMapping("/meumenu/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private RestauranteRepository repositoryRestaurante;
    @Autowired
    private FavoritoRepository repositoryFavorito;

    @PostMapping("/cadastrar")
    @Operation(summary = "Metodo de cadastrar usuario", description = "Create User MeuMenu", responses = {@ApiResponse(responseCode = "200", description = "Sucesso usuario meu menu criado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso usuario meu menu criado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<UsuarioDTO> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados) {
        repository.save(new Usuario(dados));
        List<Usuario> tempUsuario = repository.findAll();
        UsuarioDTO usuario = new UsuarioDTO(tempUsuario.get(tempUsuario.size() - 1).getId(), dados.nome(), dados.sobrenome(), dados.email(), dados.tipoComidaPreferida().name());
        return ResponseEntity.status(200).body(usuario);
    }

    @GetMapping
    @Operation(summary = "Metodo de listar todos os usuarios", description = "List Users MeuMenu", responses = {@ApiResponse(responseCode = "200", description = "Sucesso lista retornada!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada!\"}"),})), @ApiResponse(responseCode = "204", description = "Sucesso lista retornada mas vazia!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 204', \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada mas vazia!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<List<UsuarioDTO>> listar() {
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        List<Usuario> tempUsuario = repository.findAll();
        for (Usuario u : tempUsuario) {
            usuariosDTO.add(new UsuarioDTO(u.getId(), u.getNome(), u.getSobrenome(), u.getEmail(), u.getTipoComidaPreferida().name()));
        }
        return ResponseEntity.status(200).body(usuariosDTO);
    }

    @GetMapping("{id}")
    @Operation(summary = "Metodo de listar usuario por id", description = "Listar o usuario meu menu especificado por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso listou o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso listou o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<UsuarioDTO> listarPorId(@PathVariable Integer id) {
        List<Usuario> tempUsuario = repository.findAll();
        for (Usuario u : tempUsuario) {
            if (u.getId().equals(id)) {
                UsuarioDTO usuario = new UsuarioDTO(u.getId(), u.getNome(), u.getSobrenome(), u.getEmail(), u.getTipoComidaPreferida().name());
                return ResponseEntity.status(200).body(usuario);
            }
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Metodo de atualizar dados do usuario", description = "Atualizar o usuario meu menu especificado por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso atualizou o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso atualizou o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<String> atualizar(@RequestBody @Valid Usuario dados, @PathVariable int id) {
        if (repository.existsById(id)) {
            Usuario usuario = repository.findById(id).orElseThrow();
            usuario.setTipoComidaPreferida(dados.getTipoComidaPreferida());
            repository.save(usuario);
            return ResponseEntity.status(200).body("Usuário atualizado com sucesso");
        }
        return ResponseEntity.status(404).body("Usuário não encontrado");
    }

    @PostMapping("/logar")
    @Operation(summary = "Metodo de logar", description = "Atualizar o usuario meu menu especificado por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso atualizou o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso atualizou o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<String> logar(@RequestBody Usuario dados) {

        List<Usuario> listaUsuario = repository.findAll();

        for (Usuario b : listaUsuario) {
            if (b.getEmail().equals(dados.getEmail()) && b.getSenha().equals(dados.getSenha())) {
                return ResponseEntity.status(200).body("Login efetuado com sucesso");
            }
        }
        return ResponseEntity.status(401).body("Email e senha incorretos");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Metodo de deletar usuario por id", description = "Deletar o usuario MeuMenu especificado por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso deletou o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso deletou o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<String> deletar(@PathVariable int id) {
        if (repository.existsById(id)) {
            Usuario usuario = repository.findById(id).orElseThrow();
            repository.delete(usuario);
            return ResponseEntity.status(200).body("Usuário deletado com sucesso");
        }
        return ResponseEntity.status(404).body("Usuário não encontrado");
    }

    @GetMapping("/recomendar/{id}")
    @Operation(summary = "Metodo de recomendar restaurantes por tipo de comida preferida do usuario", description = "Recomenda restaurante para o usuario MeuMenu", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante recomendando para o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante recomendando para o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<List<RestauranteDTO>> recomendar(@PathVariable int id) {
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        List<Usuario> listaUsuario = repository.findAll();
        List<Restaurante> listaRestaurante = repositoryRestaurante.findAll();
        List<RestauranteDTO> restauranteDTO = new ArrayList<>();

        if (!repository.existsById(id)) {
            return ResponseEntity.status(404).build();
        }

        for (Usuario usuario : listaUsuario) {
            if (usuario.getId() == id) {
                restauranteDTO = (usuario.recomendar(listaUsuario, listaRestaurante, id));
            }
        }
        if (restauranteDTO.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(restauranteDTO);
    }

    @PostMapping("/favoritar")
    @Operation(summary = "Metodo de favoritar restaurantes", description = "Favorita restaurante para o usuario MeuMenu", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante favoritado para o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante favoritado para o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<List<Favorito>> favoritar(@RequestBody @Valid Favorito dados) {
        repositoryFavorito.save(new Favorito(dados.getFk_usuario(), dados.getFk_restaurante()));
        List<Favorito> l = repositoryFavorito.findAll();
        return ResponseEntity.status(200).body(l);
    }

    @DeleteMapping("/favoritar")
    @Operation(summary = "Metodo de desfavoritar restaurantes", description = "Desfavorita restaurante para o usuario MeuMenu", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante desfavoritado para o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante desfavoritado para o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    public ResponseEntity<Void> desfavoritar(@RequestBody @Valid Favorito dados) {
        FavoritoId favorito = new FavoritoId(dados.getFk_usuario(), dados.getFk_restaurante());
        if (repositoryFavorito.existsById(favorito)) {
            repositoryFavorito.deleteById(favorito);
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.status(404).build();
    }

    //GRAVAR ARQUIVO CSV ---------------
    @GetMapping("/download/{id}")
    public ResponseEntity<String> gravaArquivoCsv(@PathVariable int id) {
        FileWriter arq = null;
        Formatter saida = null;
        Boolean deuRuim = false;

        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        List<Usuario> tempUsuario = repository.findAll();
        Optional<Restaurante> idRestaurante = repositoryRestaurante.findById(id);
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
