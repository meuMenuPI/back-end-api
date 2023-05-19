package meumenu.application.meumenu.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository  extends JpaRepository<Review, ReviewId> {
    List<Review> findByFkRestaurante(Integer fkRestaurante);
}
