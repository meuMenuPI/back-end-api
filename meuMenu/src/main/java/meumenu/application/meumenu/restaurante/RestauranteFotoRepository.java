package meumenu.application.meumenu.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RestauranteFotoRepository extends JpaRepository<RestauranteFoto, Integer> {
    List<RestauranteFoto> findByFkRestaurante(int id);

    int countByFkRestaurante(int id);
}
