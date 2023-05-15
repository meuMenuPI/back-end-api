package meumenu.application.meumenu.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository  extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmailAndSenha(String email, String senha);

}
