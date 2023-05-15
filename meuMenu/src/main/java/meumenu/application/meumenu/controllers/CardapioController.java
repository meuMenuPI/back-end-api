package meumenu.application.meumenu.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import meumenu.application.meumenu.cardapio.Cardapio;
import meumenu.application.meumenu.cardapio.CardapioRepository;
import meumenu.application.meumenu.cardapio.DadosCadastroCardapio;
import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/meumenu/cardapios")
public class CardapioController {
    @Autowired
    private CardapioRepository cardapioRepository;

    @PostMapping
    @Operation(summary = "Metodo de cadastrar prato", description = "Create Prato MeuMenu", responses = {@ApiResponse(responseCode = "200", description = "Sucesso prato criado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso prato criado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Cardapio> cadastrar(@RequestBody @Valid DadosCadastroCardapio dados){
        cardapioRepository.save(new Cardapio(dados));
        List<Cardapio> cardapios = cardapioRepository.findAll();
        Cardapio cardapio = new Cardapio(cardapios.get(cardapios.size()-1).getId(),dados.fk_restaurante() ,dados.nome(), dados.descricao(), dados.preco(), dados.estiloGastronomico(), dados.qtd_carboidratos(), dados.qtd_proteinas(), dados.qtd_acucar(), dados.qtd_calorias(), dados.qtd_gorduras_totais());
        return ResponseEntity.status(200).body(cardapio);
    }

    @GetMapping
    @Operation(summary = "Metodo de listar todos os pratos do restaurante", description = "List cardapio", responses = {@ApiResponse(responseCode = "200", description = "Sucesso lista retornada!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada!\"}"),})), @ApiResponse(responseCode = "204", description = "Sucesso lista retornada mas vazia!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 204', \"Status\" : \"Ok!\", \"Message\" :\"Sucesso lista retornada mas vazia!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<List<Cardapio>> listar(@RequestParam Integer id){
        List<Cardapio> cardapios = cardapioRepository.findByRestauranteLista(id);
        if(cardapios.isEmpty()){
            return ResponseEntity.status(204).build();
    }
        return ResponseEntity.status(200).body(cardapios);
    }

    @PutMapping()
    @Operation(summary = "Metodo de atualizar dados do prato", description = "Atualiza prato por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso prato atualizado!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso prato atualizado!\"}"),})), @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 400, \"Status\" : \"Erro\", \"Message\" :\"Bad request\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Cardapio> atualizar(@RequestBody @Valid Cardapio dados) {
        if (cardapioRepository.existsById(dados.getId())) {
            Cardapio cardapio = cardapioRepository.findById(dados.getId()).orElseThrow();
            if (dados.getNome() != null) {
                cardapio.setNome(dados.getNome());
            }
            if (dados.getPreco() != null) {
                cardapio.setPreco(dados.getPreco());
            }
            if (dados.getEstiloGastronomico() != null) {
                cardapio.setEstiloGastronomico(dados.getEstiloGastronomico());
            }
            if(dados.getQtd_carboidratos() != null){
                cardapio.setQtd_carboidratos(dados.getQtd_carboidratos());
            }
            if(dados.getQtd_proteinas() != null){
                cardapio.setQtd_proteinas(dados.getQtd_proteinas());
            }
            if(dados.getQtd_acucar() != null){
                cardapio.setQtd_acucar(dados.getQtd_acucar());
            }
            if(dados.getQtd_calorias() != null){
                cardapio.setQtd_calorias(dados.getQtd_calorias());
            }
            if(dados.getQtd_gorduras_totais() != null){
                cardapio.setQtd_gorduras_totais(dados.getQtd_gorduras_totais());
            }
            cardapioRepository.save(cardapio);
            return ResponseEntity.status(200).body(cardapio);
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Metodo de deletar prato por id", description = "Deletar o prato do restaurante especificado por id", responses = {@ApiResponse(responseCode = "200", description = "Sucesso deletou o prato!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso deletou o prato!\"}"),})), @ApiResponse(responseCode = "404", description = "Prato não encontrado", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 404, \"Status\" : \"Erro\", \"Message\" :\"Prato não encontrado\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<String> deletar(@PathVariable int id) {
        if (cardapioRepository.existsById(id)) {
            Cardapio cardapio = cardapioRepository.findById(id).orElseThrow();
            cardapioRepository.delete(cardapio);
            return ResponseEntity.status(200).body("Prato deletado com sucesso");
        }
        return ResponseEntity.status(404).body("Prato não encontrado");
    }

    @GetMapping("/ordernar/preco-crescente")
    @Operation(summary = "Metodo de ordenar prato preço crescente", description = "Ordenar pratos do restaurante por preço crescente", responses = {@ApiResponse(responseCode = "200", description = "Sucesso deletou o prato!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso!\"}"),})), @ApiResponse(responseCode = "204", description = "Nenhum prato cadastrado no seu restaurante!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 204, \"Status\" : \"Erro\", \"Message\" :\"Nenhum prato cadastrado no seu restaurante!\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Cardapio[]> ordenarPrecoCrescente(@RequestParam Integer id){
        Cardapio[] cardapio = cardapioRepository.findByRestauranteVetor(id);
        if(cardapio.length == 0){
            return ResponseEntity.status(204).build();
        }
        boolean troca = true;
        Cardapio aux;
        while (troca) {
            troca = false;
            for (int i = 0; i < cardapio.length - 1; i++) {
                if (cardapio[i].getPreco() > cardapio[i + 1].getPreco()) {
                    aux = cardapio[i];
                    cardapio[i] = cardapio[i + 1];
                    cardapio[i + 1] = aux;
                    troca = true;
                }
            }
        }
        return ResponseEntity.status(200).body(cardapio);
    }

    @GetMapping("/ordernar/preco-decrescente")
    @Operation(summary = "Metodo de ordenar prato preço decrescente", description = "Ordenar pratos do restaurante por preço decrescente", responses = {@ApiResponse(responseCode = "200", description = "Sucesso deletou o prato!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Sucesso!\"}"),})), @ApiResponse(responseCode = "204", description = "Nenhum prato cadastrado no seu restaurante!", content = @Content(mediaType = "application/json", examples = {@ExampleObject(value = "{\"code\" : 204, \"Status\" : \"Erro\", \"Message\" :\"Nenhum prato cadastrado no seu restaurante!\"}"),}))})
    @Transactional
    @CrossOrigin
    public ResponseEntity<Cardapio[]> ordenarPrecoDecrescente(@RequestParam Integer id){
        Cardapio[] cardapio = cardapioRepository.findByRestauranteVetor(id);
        if(cardapio.length == 0){
            return ResponseEntity.status(204).build();
        }
        boolean troca = true;
        Cardapio aux;
        while (troca) {
            troca = false;
            for (int i = 0; i < cardapio.length - 1; i++) {
                if (cardapio[i].getPreco() < cardapio[i + 1].getPreco()) {
                    aux = cardapio[i];
                    cardapio[i] = cardapio[i + 1];
                    cardapio[i + 1] = aux;
                    troca = true;
                }
            }
        }
        return ResponseEntity.status(200).body(cardapio);
    }
}
