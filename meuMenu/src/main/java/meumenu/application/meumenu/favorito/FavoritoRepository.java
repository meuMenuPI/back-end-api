package meumenu.application.meumenu.favorito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavoritoRepository extends JpaRepository<Favorito, FavoritoId> {
    @Query(value = "select email from usuario as us join favorito as fav on fk_usuario = us.id where fav.fk_restaurante = ?1", nativeQuery = true)
    String [] findAllFavoritos (Integer fk_restaurante);

    @Query(value = "select count(*) from favorito where fk_usuario = ?1 and fk_restaurante = ?2", nativeQuery = true)
    Integer [] FindSeguindo (Integer fk_usuario, Integer fk_restaurante);

 //   @Query("select count(*) from usuario as us join favorito as fav on fk_usuario = us.id where fav.fk_restaurante = ?")
 //   Integer tamanho (Integer id);
}
