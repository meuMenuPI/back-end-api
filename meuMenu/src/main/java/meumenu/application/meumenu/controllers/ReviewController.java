package meumenu.application.meumenu.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import meumenu.application.meumenu.cardapio.Cardapio;
import meumenu.application.meumenu.cardapio.DadosCadastroCardapio;
import meumenu.application.meumenu.restaurante.DadosCadastroRestaurante;
import meumenu.application.meumenu.restaurante.RestauranteDTO;
import meumenu.application.meumenu.review.DadosCadastroReview;
import meumenu.application.meumenu.review.Review;
import meumenu.application.meumenu.review.ReviewRepository;
import meumenu.application.meumenu.services.RestauranteService;
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
    @Operation(summary = "Metodo de cadastrar prato", description = "Create Prato MeuMenu", responses = {@ApiResponse(responseCode = "200", description = "Sucesso prato criado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso prato criado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<DadosCadastroReview> cadastrarReview(@RequestBody @Valid DadosCadastroReview dados){
        this.service.cadastrarReview(dados);
        return ResponseEntity.ok().body(dados);
    }

}
