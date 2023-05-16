package meumenu.application.meumenu.review;

import meumenu.application.meumenu.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository  extends JpaRepository<Review, ReviewId> {
}
