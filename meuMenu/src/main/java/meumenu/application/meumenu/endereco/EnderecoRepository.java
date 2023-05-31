package meumenu.application.meumenu.endereco;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository <Endereco, Integer> {

    @Query(value = "select * from endereco where fk_restaurante = ?1", nativeQuery = true)
    Optional<Endereco> findByFkRestaurante(Integer fk_restaurante);
}
