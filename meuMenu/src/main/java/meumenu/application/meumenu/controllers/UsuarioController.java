package meumenu.application.meumenu.controllers;

import jakarta.validation.Valid;
import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.restaurante.RestauranteRepository;
import meumenu.application.meumenu.usuario.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import meumenu.application.meumenu.usuario.DadosCadastroUsuario;
import meumenu.application.meumenu.usuario.Usuario;
import meumenu.application.meumenu.usuario.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/meumenu/usuarios")
public class UsuarioController {
@Autowired
    private UsuarioRepository repository;
@Autowired
    private RestauranteRepository repositoryRestaurante;

    @PostMapping("/cadastrar")
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroUsuario dados){
        repository.save(new Usuario(dados));
    }

    @GetMapping
    @Transactional
    public List<UsuarioDTO> listar(){
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        List<Usuario> tempUsuario = repository.findAll();
        for(Usuario u : tempUsuario){
            usuariosDTO.add(new UsuarioDTO(u.getNome(), u.getSobrenome(), u.getEmail(), u.getTipoComidaPreferida().name()));
        }
        return usuariosDTO;
    }

    @PutMapping("/{id}")
    @Transactional
    public String atualizar(@RequestBody @Valid Usuario dados, @PathVariable int id){
        Usuario usuario = repository.findById(id).orElseThrow();
        usuario.setTipoComidaPreferida(dados.getTipoComidaPreferida());
        repository.save(usuario);
        return "Usuário atualizado com sucesso";
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

    @DeleteMapping("/{id}")
    @Transactional
    public String deletar(@PathVariable int id){
        Usuario usuario = repository.findById(id).orElseThrow();
        repository.delete(usuario);

        return "Usuário deletado com sucesso";
    }

    @GetMapping("/recomendar/{id}")
    @Transactional
    public List<Usuario> recomendar(@PathVariable int id){
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();

        List<Usuario> listaUsuario = repository.findAll();
        List<Restaurante> listaRestaurante = repositoryRestaurante.findAll();

        for(Usuario b : listaUsuario){
            if(b.getId() == id){
                return b.recomendar(listaUsuario, listaRestaurante, id);
            }
        }
        return null;
    }





}
