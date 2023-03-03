package meumenu.application.meumenu.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository  extends JpaRepository<Usuario, Integer> {
}
