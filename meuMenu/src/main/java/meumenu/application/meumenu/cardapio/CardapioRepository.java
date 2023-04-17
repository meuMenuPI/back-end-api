package meumenu.application.meumenu.cardapio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardapioRepository extends JpaRepository<Cardapio, Integer> {
    @Query(value = "select * from cardapio  where fk_restaurante = ?1", nativeQuery = true)
    List<Cardapio> findByRestauranteLista (Integer fk_restaurante);

    @Query(value = "select * from cardapio  where fk_restaurante = ?1", nativeQuery = true)
    Cardapio[] findByRestauranteVetor (Integer fk_restaurante);
}
