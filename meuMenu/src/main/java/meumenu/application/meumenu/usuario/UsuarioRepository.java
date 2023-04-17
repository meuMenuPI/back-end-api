package meumenu.application.meumenu.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository  extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

}
