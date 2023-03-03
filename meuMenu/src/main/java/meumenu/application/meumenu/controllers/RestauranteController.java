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


}

