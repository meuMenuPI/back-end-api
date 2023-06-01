package meumenu.application.meumenu.controllers;


import com.azure.core.annotation.Headers;
import com.azure.core.http.rest.Response;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import meumenu.application.meumenu.favorito.Favorito;
import meumenu.application.meumenu.favorito.FavoritoId;
import meumenu.application.meumenu.favorito.FavoritoRepository;
import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.restaurante.RestauranteDTO;
import meumenu.application.meumenu.restaurante.RestauranteRepository;
import meumenu.application.meumenu.usuario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
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
    @CrossOrigin
    public ResponseEntity<UsuarioDTO> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados) {
        repository.save(new Usuario(dados));
        List<Usuario> tempUsuario = repository.findAll();
        UsuarioDTO usuario = new UsuarioDTO(tempUsuario.get(tempUsuario.size() - 1).getId(), dados.nome(), dados.sobrenome(), dados.email(), dados.tipoComidaPreferida().name(), null);
        return ResponseEntity.status(200).body(usuario);
    }

    @GetMapping
    @Operation(summary = "Metodo de listar todos os usuarios", description = "List Users MeuMenu", responses = {@ApiResponse(responseCode = "200", description = "Sucesso lista retornada!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada!\"}"),})), @ApiResponse(responseCode = "204", description = "Sucesso lista retornada mas vazia!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 204', \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada mas vazia!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<List<UsuarioDTO>> listar() {
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        List<Usuario> tempUsuario = repository.findAll();
        for (Usuario u : tempUsuario) {
            usuariosDTO.add(new UsuarioDTO(u.getId(), u.getNome(), u.getSobrenome(), u.getEmail(), u.getTipoComidaPreferida().name(), u.getFotoPerfil()));
        }
        return ResponseEntity.status(200).body(usuariosDTO);
    }

    @GetMapping("{id}")
    @Operation(summary = "Metodo de listar usuario por id", description = "Listar o usuario meu menu especificado por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso listou o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso listou o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<UsuarioDTO> listarPorId(@PathVariable Integer id) {
        Optional<Usuario> u = repository.findById(id);

        if (u.isPresent()) {
            UsuarioDTO usuario = new UsuarioDTO(u.get().getId(), u.get().getNome(), u.get().getSobrenome(), u.get().getEmail(), u.get().getTipoComidaPreferida().name(), u.get().getFotoPerfil());
            return ResponseEntity.status(200).body(usuario);
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Metodo de atualizar dados do usuario", description = "Atualizar o usuario meu menu especificado por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso atualizou o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso atualizou o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Usuario> atualizar(@RequestBody @Valid Usuario dados, @PathVariable int id) {
        if (repository.existsById(id)) {
            Usuario usuario = repository.findById(id).orElseThrow();
            if(dados.getTipoComidaPreferida() != null) {
                usuario.setTipoComidaPreferida(dados.getTipoComidaPreferida());}
            if(dados.getNome() != null) {
                usuario.setNome(dados.getNome());}
            if(dados.getSobrenome() != null) {
                usuario.setSobrenome(dados.getSobrenome());}
            if(dados.getEmail() != null) {
                usuario.setEmail(dados.getEmail());}
            repository.save(usuario);
            return ResponseEntity.status(200).body(usuario);
        }
        return ResponseEntity.status(404).build();
    }

    @PostMapping("/logar")
    @Operation(summary = "Metodo de logar", description = "logar o usuario na aplicação", responses = {
            @ApiResponse(responseCode = "200", description = "Sucesso, usuário logado!", content = @Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso, usuário logado!\"}"),})),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado!", content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = "{\"code\" : 404, \"Status\" : \"Erro!\", \"Message\" :\"Usuário não encontrado!\"}"),})),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<UsuarioDTO> logar(@RequestBody UsuarioDtoLogar dados) {

        Optional<Usuario> b = repository.findByEmailAndSenha(dados.getEmail(),dados.getSenha());

        if(b == null){
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(new UsuarioDTO(b.get().getId(), b.get().getNome(), b.get().getSobrenome(), b.get().getEmail(), b.get().getTipoComidaPreferida().name(), b.get().getFotoPerfil()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Metodo de deletar usuario por id", description = "Deletar o usuario MeuMenu especificado por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso deletou o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso deletou o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<String> deletar(@PathVariable int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.status(200).body("Usuário deletado com sucesso");
        }
        return ResponseEntity.status(404).body("Usuário não encontrado");
    }

    @GetMapping("/recomendar/{id}")
    @Operation(summary = "Metodo de recomendar restaurantes por tipo de comida preferida do usuario", description = "Recomenda restaurante para o usuario MeuMenu", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante recomendando para o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante recomendando para o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
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

    @GetMapping("/favoritar")
    @Operation(summary = "Metodo de listar os usuarios favoritados", description = "listar se o usuario favoritou o restaurante", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante favoritado para o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante favoritado para o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Integer[]> getfavoritar(@RequestParam Integer fk_usuario, @RequestParam Integer fk_restaurante) {
        Integer seguindo [] = repositoryFavorito.FindSeguindo(fk_usuario, fk_restaurante);
        if(seguindo[0] == 0){
            return ResponseEntity.status(204).body(seguindo);
        }
        return ResponseEntity.status(200).body(seguindo);
    }

    @PostMapping("/favoritar")
    @Operation(summary = "Metodo de favoritar restaurantes", description = "Favorita restaurante para o usuario MeuMenu", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante favoritado para o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante favoritado para o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<List<Favorito>> favoritar(@RequestBody @Valid Favorito dados) {
        repositoryFavorito.save(new Favorito(dados.getFk_usuario(), dados.getFk_restaurante()));
        List<Favorito> l = repositoryFavorito.findAll();
        return ResponseEntity.status(200).body(l);
    }

    @DeleteMapping("/favoritar")
    @Operation(summary = "Metodo de desfavoritar restaurantes", description = "Desfavorita restaurante para o usuario MeuMenu", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante desfavoritado para o usuario MeuMenu!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante desfavoritado para o usuario MeuMenu!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Void> desfavoritar(@RequestParam Integer fk_usuario, @RequestParam Integer fk_restaurante ) {
        FavoritoId favorito = new FavoritoId(fk_usuario, fk_restaurante);
        if (repositoryFavorito.existsById(favorito)) {
            repositoryFavorito.deleteById(favorito);
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("/foto-usuario/{id}")
    @CrossOrigin
    public ResponseEntity<String> cadastroFoto(@PathVariable int id, @RequestParam MultipartFile imagem) throws IOException {

        byte[] bytes = imagem.getBytes();
        if (bytes.length == 0){
            throw new IOException("Imagem não contem bytes");
        }

        Optional<Usuario> usuario = repository.findById(id);

        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuario não encontrado");
        }

        String fileName = LocalDateTime.now() + imagem.getOriginalFilename();

        String constr = "DefaultEndpointsProtocol=https;AccountName=meumenuimagens;AccountKey=R9lel0MHe6" +
                "BQTZj3c7dDQYXKKGiC75NpsmLi/IqBChb4NAGFT5kheiorbVyx/pSAo9VC5e/Ktkju+AStGIYs7w==;Endpoint" +
                "Suffix=core.windows.net";

        BlobContainerClient container = new BlobContainerClientBuilder()
                .connectionString(constr).containerName("foto-suario").buildClient();

        BlobClient blob = container.getBlobClient(fileName);


        Response<BlockBlobItem> response = blob.uploadWithResponse(
                new BlobParallelUploadOptions(new ByteArrayInputStream(bytes), bytes.length),
                Duration.ofHours(5),
                null);

        if (response.getStatusCode() != 201) {
            throw new IOException("request failed");
        }

        usuario.get().setFotoPerfil(fileName);

        repository.save(usuario.get());

        return ResponseEntity.status(200).body(usuario.get().getFotoPerfil());
    }

}
