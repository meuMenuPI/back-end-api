package meumenu.application.meumenu.favorito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavoritoRepository extends JpaRepository<Favorito, FavoritoId> {
    @Query(value = "select email from usuario as us join favorito as fav on fk_usuario = us.id where fav.fk_restaurante = ?1", nativeQuery = true)
    String [] findAllFavoritos (Integer fk_restaurante);

 //   @Query("select count(*) from usuario as us join favorito as fav on fk_usuario = us.id where fav.fk_restaurante = ?")
 //   Integer tamanho (Integer id);
}
