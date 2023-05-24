package meumenu.application.meumenu.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Integer>{
    Restaurante findByCnpj(String cnpj);

    @Query(value = "select * from restaurante  where especialidade = ?1", nativeQuery = true)
    List<Restaurante> findByRestauranteEspecialidade  (String especialidade);

    List<Restaurante> findByBeneficio(boolean beneficio);

}
