package meumenu.application.meumenu.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import meumenu.application.meumenu.review.DadosCadastroReview;
import meumenu.application.meumenu.review.Review;
import meumenu.application.meumenu.review.ReviewRepository;
import meumenu.application.meumenu.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meumenu/reviews")
public class ReviewController {
    @Autowired
    private ReviewRepository repositoryReview;

    @Autowired
    private ReviewService service;

    @PostMapping
    @Operation(summary = "Metodo de cadastrar review", description = "Create Review MeuMenu", responses = {@ApiResponse(responseCode = "200", description = "Sucesso review criado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso review criado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<DadosCadastroReview> cadastrarReview(@RequestBody @Valid DadosCadastroReview dados){
        this.service.cadastrarReview(dados);
        return ResponseEntity.ok().body(dados);
    }

    @GetMapping
    @Operation(summary = "Metodo de listar review por id", description = "Lista review por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso review listado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso restaurante listado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<List<Review>> listarPorRestaurante(@RequestParam Integer fkRestaurante) {
        return ResponseEntity.ok(this.service.listarPorRestaurante(fkRestaurante));
    }

}
