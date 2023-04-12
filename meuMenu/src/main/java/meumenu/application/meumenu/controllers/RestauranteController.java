package meumenu.application.meumenu.controllers;
import jakarta.validation.Valid;
import meumenu.application.meumenu.assossiativas.FavoritoRepository;
import meumenu.application.meumenu.restaurante.*;
import meumenu.application.meumenu.usuario.Usuario;
import meumenu.application.meumenu.usuario.UsuarioDTO;
import meumenu.application.meumenu.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/meumenu/restaurantes")
public class RestauranteController {
    @Autowired
    private RestauranteRepository repository;

    @Autowired
    private UsuarioRepository repositoryUsuario;

    @Autowired
    private FavoritoRepository repositoryFavorito;

    // biblioteca responsavel por mandar o email
    @Autowired private JavaMailSender javaMailSender;

    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity <RestauranteDTO> cadastrar(@RequestBody @Valid DadosCadastroRestaurante dados){
        repository.save(new Restaurante(dados));
        List<Restaurante> tempRestaurante = repository.findAll();
        RestauranteDTO restaurante = new RestauranteDTO(tempRestaurante.get(tempRestaurante.size()-1).getId(), dados.nome(), dados.especialidade().name(), dados.telefone(), dados.site() ,dados.estrela());
        return ResponseEntity.status(200).body(restaurante);
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<RestauranteDTO>>  listar(){
        List<RestauranteDTO> restauranteDTO = new ArrayList<>();
        List<Restaurante> tempRestaurante = repository.findAll();
        for(Restaurante r : tempRestaurante){
            restauranteDTO.add(new RestauranteDTO(r.getId(),r.getNome(), r.getEspecialidade().name(), r.getTelefone(), r.getSite(), r.getEstrela()));
        }
        return ResponseEntity.status(200).body(restauranteDTO);
    }

    @GetMapping("{id}")
    @Transactional
    public ResponseEntity<RestauranteDTO> listarPorId(@PathVariable Integer id){
        List<Restaurante> tempRestaurante = repository.findAll();
        for(Restaurante r : tempRestaurante){
            if(r.getId().equals(id)){
                RestauranteDTO restaurante = new RestauranteDTO(r.getId(),r.getNome(), r.getEspecialidade().name(), r.getTelefone(), r.getSite(), r.getEstrela());
                return ResponseEntity.status(200).body(restaurante);
            }
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("/{id}")
    @Transactional
    public  ResponseEntity<String> atualizar (@RequestBody @Valid Restaurante dados, @PathVariable int id){
        if(repository.existsById(id)){
            Restaurante restaurante = repository.findById(id).orElseThrow();
            if(dados.getNome() != null){
                restaurante.setNome(dados.getNome());
            }
            if(dados.getSite()!= null){
                restaurante.setSite(dados.getSite());
            }
            if(dados.getTelefone()!= null){
                restaurante.setTelefone(dados.getTelefone());
            }
            if(dados.getEstrela() != null){
                restaurante.setEstrela(dados.getEstrela());
            }
            repository.save(restaurante);
            return ResponseEntity.status(200).body("Restaurante atualizado com sucesso");
        }
        return ResponseEntity.status(404).body("Restaurante não encontrado");
    }

    @DeleteMapping("/{id}")
    @Transactional
    public  ResponseEntity<String> deletar (@PathVariable int id){
        if(repository.existsById(id)){
            Restaurante restaurante = repository.findById(id).orElseThrow();
            repository.delete(restaurante);
            return ResponseEntity.status(200).body("Restaurante deletado com sucesso");
        }
        return ResponseEntity.status(404).body("Restaurante não encontrado");
    }

    @GetMapping("/recomendar/{id}")
    @Transactional
    public ResponseEntity<List<UsuarioDTO>> recomendar(@PathVariable int id){

        List<Restaurante> listaRestaurante = repository.findAll();
        List<Usuario> listaUsuario = repositoryUsuario.findAll();
        List<UsuarioDTO> usuarioDTO = new ArrayList<>();

        if(!repository.existsById(id)){
            return ResponseEntity.status(404).build();
        }

        for(Restaurante r : listaRestaurante){
            if(r.getId() == id){
                usuarioDTO = (r.recomendar(listaUsuario, listaRestaurante, id));
            }
        }
        if(usuarioDTO.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(usuarioDTO);
    }

    @PostMapping("email/{id}")
    @Transactional
        public ResponseEntity<String[]> enviarEmail(@RequestBody Email email, @PathVariable int id) {
        String vetor [] = repositoryFavorito.findAllFavoritos(id);
        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom("meumenu.contato@gmail.com");
            mailMessage.setText(email.getTexto());
            mailMessage.setSubject(email.getTitulo());

            for (int i = 0; i < vetor.length; i++) {
                mailMessage.setTo(vetor[i]);
                javaMailSender.send(mailMessage);
            }
            return ResponseEntity.status(200).body(vetor);
        }
        catch (Exception erro) {
            return ResponseEntity.status(400).body(vetor);
        }
    }
}


