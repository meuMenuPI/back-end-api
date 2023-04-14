package meumenu.application.meumenu.controllers;

import jakarta.validation.Valid;
import meumenu.application.meumenu.cardapio.Cardapio;
import meumenu.application.meumenu.cardapio.CardapioRepository;
import meumenu.application.meumenu.cardapio.DadosCadastroCardapio;
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
    @Transactional
    public ResponseEntity<Cardapio> cadastrar(@RequestBody @Valid DadosCadastroCardapio dados){
        cardapioRepository.save(new Cardapio(dados));
        List<Cardapio> cardapios = cardapioRepository.findAll();
        Cardapio cardapio = new Cardapio(cardapios.get(cardapios.size()-1).getId(),dados.fk_restaurante() ,dados.nome(), dados.preco(), dados.estiloGastronomico());
        return ResponseEntity.status(200).body(cardapio);
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<Cardapio>> listar(@RequestParam Integer id){
        List<Cardapio> cardapios = cardapioRepository.findByRestaurante(id);
        if(cardapios.isEmpty()){
            return ResponseEntity.status(204).build();
    }
        return ResponseEntity.status(200).body(cardapios);
    }
}
