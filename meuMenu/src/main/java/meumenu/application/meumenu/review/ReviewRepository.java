package meumenu.application.meumenu.review;

import meumenu.application.meumenu.cardapio.Cardapio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository  extends JpaRepository<Review, ReviewId> {
    List<Review> findByFkRestaurante(Integer fkRestaurante);
//    @Query(value = "select fk_usuario, fk_restaurante, date_format(data_hora, '%Y-%m-%d') as 'data', descricao, nt_comida, nt_ambiente, nt_atendimento  from review  where fk_restaurante = ?1", nativeQuery = true)
//    List<Review> findByFkRestaurante (Integer fk_restaurante);
}
