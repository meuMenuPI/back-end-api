package meumenu.application.meumenu.controllers;

import jakarta.validation.Valid;
import meumenu.application.meumenu.restaurante.DadosCadastroRestaurante;
import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.restaurante.RestauranteRepository;
import meumenu.application.meumenu.usuario.Usuario;
import meumenu.application.meumenu.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meumenu/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository repository;

    @Autowired
    private UsuarioRepository repositorya;


    @PostMapping("/cadastrar")
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroRestaurante dados){

        repository.save(new Restaurante(dados));

    }

    @PutMapping("/{id}")
    @Transactional
    public void atualizar(@RequestBody @Valid Restaurante dados, @PathVariable int id){
        Restaurante restaurante = repository.findById(id).orElseThrow();
        restaurante.setSite(dados.getSite());
        restaurante.setTelefone(dados.getTelefone());
        restaurante.setEstrela(dados.getEstrela());
        repository.save(restaurante);
    }

    @GetMapping("/recomendar/{id}")
    @Transactional
    public List<Usuario> recomendar(@PathVariable int id){

        List<Restaurante> listaRestaurante = repository.findAll();
        List<Usuario> listaUsuario = repositorya.findAll();

        for(Restaurante r : listaRestaurante){
            if(r.getId() == id){
                return r.recomendar(listaUsuario, listaRestaurante, id);
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deletar(@PathVariable int id){
        Restaurante restaurante = repository.findById(id).orElseThrow();
        repository.delete(restaurante);
    }


}

