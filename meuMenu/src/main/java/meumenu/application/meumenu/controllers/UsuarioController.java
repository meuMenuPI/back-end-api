package meumenu.application.meumenu.controllers;

import jakarta.validation.Valid;
import meumenu.application.meumenu.assossiativas.Favorito;
import meumenu.application.meumenu.assossiativas.FavoritoRepository;
import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.restaurante.RestauranteDTO;
import meumenu.application.meumenu.restaurante.RestauranteRepository;
import meumenu.application.meumenu.usuario.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@Autowired
    private FavoritoRepository repositoryFavorito;

    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<UsuarioDTO> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados){
        repository.save(new Usuario(dados));
        List<Usuario> tempUsuario = repository.findAll();
        UsuarioDTO usuario = new UsuarioDTO(tempUsuario.get(tempUsuario.size()-1).getId(),dados.nome(), dados.sobrenome(), dados.email(), dados.tipoComidaPreferida().name());
        return ResponseEntity.status(200).body(usuario);
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<UsuarioDTO>> listar(){
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        List<Usuario> tempUsuario = repository.findAll();
        for(Usuario u : tempUsuario){
            usuariosDTO.add(new UsuarioDTO(u.getId(),u.getNome(), u.getSobrenome(), u.getEmail(), u.getTipoComidaPreferida().name()));
        }
        return ResponseEntity.status(200).body(usuariosDTO);
    }

    @GetMapping("{id}")
    @Transactional
    public ResponseEntity<UsuarioDTO> listarPorId(@PathVariable Integer id){
        List<Usuario> tempUsuario = repository.findAll();
        for(Usuario u : tempUsuario){
            if(u.getId().equals(id)){
                UsuarioDTO usuario = new UsuarioDTO(u.getId(), u.getNome(), u.getSobrenome(), u.getEmail(), u.getTipoComidaPreferida().name());
                return ResponseEntity.status(200).body(usuario);
            }
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> atualizar(@RequestBody @Valid Usuario dados, @PathVariable int id){
        if(repository.existsById(id)){
            Usuario usuario = repository.findById(id).orElseThrow();
            usuario.setTipoComidaPreferida(dados.getTipoComidaPreferida());
            repository.save(usuario);
            return ResponseEntity.status(200).body("Usuário atualizado com sucesso");
        }
        return ResponseEntity.status(404).body("Usuário não encontrado");
    }

    @PostMapping("/logar")
    @Transactional
    public ResponseEntity<String> logar(@RequestBody Usuario dados  ){

        List<Usuario> listaUsuario = repository.findAll();

        for(Usuario b : listaUsuario){
            if(b.getEmail().equals(dados.getEmail()) && b.getSenha().equals(dados.getSenha())){
                return ResponseEntity.status(200).body("Login efetuado com sucesso");
            }
        }
        return ResponseEntity.status(401).body("Email e senha incorretos");
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deletar(@PathVariable int id){
        if(repository.existsById(id)){
            Usuario usuario = repository.findById(id).orElseThrow();
            repository.delete(usuario);
            return ResponseEntity.status(200).body("Usuário deletado com sucesso");
        }
        return ResponseEntity.status(404).body("Usuário não encontrado");
    }

    @GetMapping("/recomendar/{id}")
    @Transactional
    public ResponseEntity<List<RestauranteDTO>> recomendar(@PathVariable int id){
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        List<Usuario> listaUsuario = repository.findAll();
        List<Restaurante> listaRestaurante = repositoryRestaurante.findAll();
        List<RestauranteDTO> restauranteDTO = new ArrayList<>();

        if(!repository.existsById(id)){
            return ResponseEntity.status(404).build();
        }

        for(Usuario usuario : listaUsuario){
            if(usuario.getId() == id){
                restauranteDTO = (usuario.recomendar(listaUsuario, listaRestaurante, id));
            }
        }
        if(restauranteDTO.isEmpty()){
            return ResponseEntity.status(204).build();
        }
            return ResponseEntity.status(200).body(restauranteDTO);
    }

    @PostMapping("/favoritar")
    @Transactional
    public ResponseEntity<List<Favorito>> favoritar(@RequestBody @Valid Favorito dados){
        repositoryFavorito.save(new Favorito(dados.getFk_usuario(), dados.getFk_restaurante()));
        List<Favorito> l = repositoryFavorito.findAll();
        return ResponseEntity.status(200).body(l);
    }
}
