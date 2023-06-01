package meumenu.application.meumenu.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import meumenu.application.meumenu.endereco.DadosCadastroEndereco;
import meumenu.application.meumenu.endereco.Endereco;
import meumenu.application.meumenu.endereco.EnderecoRepository;
import meumenu.application.meumenu.exceptions.NaoEncontradoException;
import meumenu.application.meumenu.favorito.FavoritoRepository;
import meumenu.application.meumenu.restaurante.*;
import meumenu.application.meumenu.services.RestauranteService;
import meumenu.application.meumenu.usuario.UsuarioDTO;
import meumenu.application.meumenu.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;

@Tag(name = "Documentação dos end-points de restaurantes", description = "Documentação viva dos restaurantes feita via swagger")
@RestController
@RequestMapping("/meumenu/restaurantes")
@CrossOrigin
public class RestauranteController {
    @Autowired
    private RestauranteRepository repository;

    @Autowired
    private UsuarioRepository repositoryUsuario;

    @Autowired
    private FavoritoRepository repositoryFavorito;
    @Autowired
    private RestauranteService service;

    @Autowired
    private RestauranteFotoRepository repositoryFoto;

    @Autowired
    private EnderecoRepository repositoryEndereco;

    // biblioteca responsavel por mandar o email
    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/cadastrar")
    @Operation(summary = "Metodo de cadastrar restaurante", description = "Cadastra restaurante", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante cadastrado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante cadastrado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<RestauranteDTO> cadastrar(@RequestBody @Valid DadosCadastroRestaurante dados) {
        this.service.cadastrar(dados);
            RestauranteDTO restaurante3 = new RestauranteDTO(repository.findByCnpj(dados.cnpj()).getId(),
                repository.findByCnpj(dados.cnpj()).getNome(), repository.findByCnpj(dados.cnpj()).getEspecialidade().name(),repository.findByCnpj(dados.cnpj()).isBeneficio(),
                repository.findByCnpj(dados.cnpj()).getTelefone(), repository.findByCnpj(dados.cnpj()).getSite(),
                dados.estrela());
        return ResponseEntity.created(null).body(restaurante3);
    }

    @GetMapping
    @Operation(summary = "Metodo de listar todos os restaurantes", description = "Lista todos restaurantes", responses = {@ApiResponse(responseCode = "200", description = "Sucesso lista retornada!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada!\"}"),})), @ApiResponse(responseCode = "204", description = "Sucesso lista retornada mas vazia!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 204', \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada mas vazia!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<List<RestauranteDTO>> listar() {
        return ResponseEntity.ok(this.service.listar());
    }

    @GetMapping("{id}")
    @Operation(summary = "Metodo de listar restaurante por id", description = "Lista restaurante por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante listado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante listado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<RestauranteDTO> listarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(this.service.listarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Metodo de atualizar dados do restaurante", description = "Atualiza restaurante por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante atualizado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante atualizado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Restaurante> atualizar(@RequestBody @Valid Restaurante dados, @PathVariable int id) {
        return ResponseEntity.ok(this.service.atualizar(dados, id));
    }

    @PutMapping("/atualizar/endereco/{id}")
    public ResponseEntity<Endereco> atualizarEndereco(@PathVariable int id, @RequestBody Endereco dados){
        this.service.atualizarEndereco(id, dados);
        return ResponseEntity.ok().body(dados);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Metodo de deletar restaurante por id", description = "Deleta restaurante por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante deletado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante deletado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        this.service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recomendar/{id}")
    @Transactional
    @CrossOrigin
    public ResponseEntity<List<UsuarioDTO>> recomendar(@PathVariable int id) {
        return ResponseEntity.ok(this.service.recomendar(id));
    }

    @PostMapping("email/{id}")
    @Operation(summary = "Metodo de enviar email para usuarios", description = "Envia email para usuarios", responses = {@ApiResponse(responseCode = "200", description = "Sucesso email enviado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso email enviado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<String[]> enviarEmail(@RequestBody Email email, @PathVariable Integer id) {
       return ResponseEntity.ok(this.service.enviarEmail(email,id));
    }

    //GRAVAR ARQUIVO CSV ---------------
    @GetMapping("/download/{id}")
    @Operation(summary = "Metodo de baixar os dados dos usuarios correspondentes", description = "Download csv dos dados dos usuarios que tem o tipo de comida favorita igual ao restaurante", responses = {@ApiResponse(responseCode = "200", description = "Sucesso email enviado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso email enviado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @CrossOrigin
    public ResponseEntity<String> gravaArquivoCsv(@PathVariable Integer id) {
        return ResponseEntity.ok(this.service.gravaArquivoCsv(id));
    }

    @PostMapping("/cadastrar/endereco")
    @Operation(summary = "Metodo de cadastrar restaurante", description = "Cadastra restaurante", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante cadastrado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante cadastrado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<DadosCadastroEndereco> cadastrarEndereco(@RequestBody DadosCadastroEndereco dados) {
       this.service.cadastrarEndereco(dados);
       return ResponseEntity.ok().body(dados);
    }

    @GetMapping("/endereco/{id}")
    @Operation(summary = "Metodo de pegar dados endereço", description = "Metodo de pegar dados endereço", responses = {@ApiResponse(responseCode = "200", description = "Sucesso endereço retornado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso endereço retornado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Endereco> listarEndereco(@PathVariable Integer id){
        Optional<Endereco> endereco = repositoryEndereco.findByFkRestaurante(id);
        return ResponseEntity.of(endereco);
    }

    @GetMapping("/filtrar/especialidade")
    @Operation(summary = "Metodo de filtrar por especialidade restaurante", description = "filtra po especialidade ", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante cadastrado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante cadastrado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<List<RestauranteReviewDTO>> filtrarPorEspecialidade(@RequestParam String especialidade){
        List<RestauranteReviewDTO> restaurantesFiltrados = repository.findByRestauranteEspecialidadeDTO(especialidade);
        if (restaurantesFiltrados.isEmpty()) throw new NaoEncontradoException("Nenhum restaurante encontrado");
        return ResponseEntity.status(200).body(restaurantesFiltrados);
    }

    @PostMapping("/foto-restaurante/{id}")
    public ResponseEntity<Boolean> cadastrarFoto(@PathVariable int id, @RequestParam MultipartFile imagem) throws IOException {

        Optional<Restaurante> restaurante = repository.findById(id);

        service.cadastrarImagem(id,imagem);

        repository.save(restaurante.get());

        return ResponseEntity.status(200).body(true);
    }

    @GetMapping("/foto-restaurante")
    public ResponseEntity<List<RestauranteFoto>> listarFotos(@RequestParam int id){
        List<RestauranteFoto> listFoto = repositoryFoto.findByFkRestaurante(id);

        return ResponseEntity.status(200).body(listFoto);
    }

    @GetMapping("/filtrar/beneficio")
    @Operation(summary = "Metodo de filtrar por beneficio restaurante", description = "filtra por beneficio ", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante cadastrado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante cadastrado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<List<RestauranteDTO>> filtrarPorBeneficio(@RequestParam boolean beneficio){
        List<Restaurante> restaurantesFiltrados = repository.findByBeneficio(beneficio);
        List<RestauranteDTO> restauranteDTO = new ArrayList<>();
        if (restaurantesFiltrados.isEmpty()) throw new NaoEncontradoException("Nenhum restaurante encontrado");
        for (Restaurante r : restaurantesFiltrados) {
            meumenu.application.meumenu.restaurante.RestauranteDTO dto = new RestauranteDTO(r.getId(), r.getNome(), r.getEspecialidade().name(), r.isBeneficio(), r.getTelefone(), r.getSite(), r.getEstrela());
            restauranteDTO.add(dto);
        }
        return ResponseEntity.status(200).body(restauranteDTO);

    }

    @GetMapping("/filtrar/bem-avaliado")
    @Operation(summary = "Metodo de filtrar restaurante por avaliação", description = "filtra por avaliação ", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante listado por avaliação!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante filtrado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public List<RestauranteReviewDTO> listarReview() {
        List<RestauranteReviewDTO> restauranteDTO = repository.findByRestauranteBemAvaliadoDTO();
        return restauranteDTO;

    }

    @GetMapping("/filtrar/uf")
    @Operation(summary = "Metodo de filtrar restaurante por uf", description = "filtra por uf ", responses = {@ApiResponse(responseCode = "200", description = "Sucesso restaurante listado por avaliação!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante filtrado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public List<RestauranteReviewDTO> listarPorUF(@RequestParam String uf) {
        List<RestauranteReviewDTO> restauranteDTO = repository.findByRestauranteUFDTO(uf);
        return restauranteDTO;
    }

    @GetMapping("/filtrar/nome-especialiade")
    @Operation(summary = "Metodo de pegar especialidades", description = "pega especialidades destintas ", responses = {@ApiResponse(responseCode = "200", description = "Lista Retornada!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Lista retornada!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public List<String> listarEspecialidades(){
        List<String> lista = repository.findByEspecialidadesDestintas();
        return lista;
    }

    @GetMapping("/filtrar/fkUsuario/{fkUsuario}")
    @Operation(summary = "Metodo de pegar restaurante fkUsuario", description = "Metodo de pegar restaurante fkUsuario", responses = {@ApiResponse(responseCode = "200", description = "Metodo de pegar restaurante fkUsuario!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Metodo de pegar restaurante fkUsuario!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public Optional<Restaurante> pegarPelaFk(@PathVariable Integer fkUsuario){
        Optional<Restaurante> restaurante = repository.findByUsuario(fkUsuario);
        return restaurante;
    }
}


