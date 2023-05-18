package meumenu.application.meumenu.restaurante;

import meumenu.application.meumenu.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteRepository extends JpaRepository<Restaurante, Integer>{
    Restaurante findByCnpj(String cnpj);
}
