package meumenu.application.meumenu.assossiativas;

import meumenu.application.meumenu.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritoRepository extends JpaRepository<Favorito, FavoritoId> {
}
