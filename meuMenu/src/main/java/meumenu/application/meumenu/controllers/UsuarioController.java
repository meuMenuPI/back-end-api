package meumenu.application.meumenu.controllers;

import jakarta.validation.Valid;
import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.restaurante.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import meumenu.application.meumenu.usuario.DadosCadastroUsuario;
import meumenu.application.meumenu.usuario.Usuario;
import meumenu.application.meumenu.usuario.UsuarioRepository;

import java.util.List;

@RestController
@RequestMapping("/meumenu/usuarios")
public class UsuarioController {
@Autowired
    private UsuarioRepository repository;
@Autowired
    private RestauranteRepository repositorya;

    @PostMapping("/cadastrar")
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroUsuario dados){

        repository.save(new Usuario(dados));

    }

    @PutMapping("/{id}")
    @Transactional
    public Usuario atualizar(@RequestBody @Valid Usuario dados, @PathVariable int id){
        Usuario usuario = repository.findById(id).orElseThrow();
        usuario.setTipoComidaPreferida(dados.getTipoComidaPreferida());
        repository.save(usuario);
        return usuario;
    }

    @PostMapping("/logar")
    @Transactional
    public Usuario logar(@RequestBody Usuario dados  ){

        List<Usuario> listaUsuario = repository.findAll();

        for(Usuario b : listaUsuario){
            if(b.getEmail().equals(dados.getEmail()) && b.getSenha().equals(dados.getSenha())){
                return b;
            }
        }
        return null;
    }

    @GetMapping("/recomendar/{id}")
    @Transactional
    public List<Usuario> recomendar(@PathVariable int id){

        List<Usuario> listaUsuario = repository.findAll();
        List<Restaurante> listaRestaurante = repositorya.findAll();

        for(Usuario b : listaUsuario){
            if(b.getId() == id){
                return b.recomendar(listaUsuario, listaRestaurante, id);
            }
        }
        return null;
    }


    @DeleteMapping("/{id}")
    @Transactional
    public void deletar(@PathVariable int id){
        Usuario usuario = repository.findById(id).orElseThrow();
        repository.delete(usuario);
    }


}
