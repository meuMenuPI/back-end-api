package meumenu.application.meumenu.services;

import jakarta.validation.Valid;
import meumenu.application.meumenu.usuario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username);
    }

    public ResponseEntity logar(UsuarioDtoLogar dados) {

        var userLogin= new UsernamePasswordAuthenticationToken(dados.getEmail(),dados.getSenha());
        var auth = this.authenticationManager.authenticate(userLogin);
        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        Optional<Usuario> b = usuarioRepository.findByEmailAndSenha(dados.getEmail(), dados.getSenha());

        if(!b.isPresent()) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    public ResponseEntity<UsuarioDTO> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(dados.senha());
        DadosCadastroUsuario dadosEncrypted = new DadosCadastroUsuario(dados.nome(),dados.sobrenome(),dados.cpf(),dados.email(),encryptedPassword,dados.tipoComidaPreferida(), dados.fotoPerfil());
        usuarioRepository.save(new Usuario(dadosEncrypted));
        List<Usuario> tempUsuario = usuarioRepository.findAll();
        UsuarioDTO usuario = new UsuarioDTO(tempUsuario.get(tempUsuario.size() - 1).getId(), dados.nome(), dados.sobrenome(), dados.email(), dados.tipoComidaPreferida().name(), null);
        return ResponseEntity.status(200).body(usuario);
    }

}
