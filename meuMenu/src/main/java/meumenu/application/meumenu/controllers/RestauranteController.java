package meumenu.application.meumenu.controllers;

import jakarta.validation.Valid;
import meumenu.application.meumenu.restaurante.DadosCadastroRestaurante;
import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.restaurante.RestauranteDTO;
import meumenu.application.meumenu.restaurante.RestauranteRepository;
import meumenu.application.meumenu.usuario.Usuario;
import meumenu.application.meumenu.usuario.UsuarioDTO;
import meumenu.application.meumenu.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/meumenu/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository repository;

    @Autowired
    private UsuarioRepository repositoryUsuario;


    @PostMapping("/cadastrar")
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroRestaurante dados){

        repository.save(new Restaurante(dados));

    }

    @GetMapping
    @Transactional
    public List<RestauranteDTO> listar(){
        List<RestauranteDTO> restauranteDTO = new ArrayList<>();
        List<Restaurante> tempRestaurante = repository.findAll();
        for(Restaurante r : tempRestaurante){
            restauranteDTO.add(new RestauranteDTO(r.getNome(), r.getEspecialidade().name(), r.getTelefone(), r.getSite(), r.getEstrela()));
        }
        return restauranteDTO;
    }

    @PutMapping("/{id}")
    @Transactional
    public String atualizar(@RequestBody @Valid Restaurante dados, @PathVariable int id){
        Restaurante restaurante = repository.findById(id).orElseThrow();
        restaurante.setNome(dados.getNome());
        restaurante.setSite(dados.getSite());
        restaurante.setTelefone(dados.getTelefone());
        restaurante.setEstrela(dados.getEstrela());
        repository.save(restaurante);

        return "Seu restaurante foi atualizado com sucesso";
    }

    @DeleteMapping("/{id}")
    @Transactional
    public String deletar(@PathVariable int id){
        Restaurante restaurante = repository.findById(id).orElseThrow();
        repository.delete(restaurante);

        return "Seu restaurante foi deletado com sucesso";
    }

    @GetMapping("/recomendar/{id}")
    @Transactional
    public List<Usuario> recomendar(@PathVariable int id){

        List<Restaurante> listaRestaurante = repository.findAll();
        List<Usuario> listaUsuario = repositoryUsuario.findAll();

        for(Restaurante r : listaRestaurante){
            if(r.getId() == id){
                return r.recomendar(listaUsuario, listaRestaurante, id);
            }
        }
        return null;
    }




}

