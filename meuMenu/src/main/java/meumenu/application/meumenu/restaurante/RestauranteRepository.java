package meumenu.application.meumenu.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Integer>{
    Restaurante findByCnpj(String cnpj);
    @Query(value = "select  r.nome, r.id,(SELECT nome_foto FROM restaurante_foto WHERE fk_restaurante = r.id LIMIT 1) AS nomeFoto " +
            "from restaurante as r join endereco as e on r.id = e.fk_restaurante where e.uf = ?1 limit 15", nativeQuery = true)
    List<Object[]> findByRestauranteUF  (String uf);
    default List<RestauranteReviewDTO> findByRestauranteUFDTO(String uf) {
        List<Object[]> results = findByRestauranteUF(uf);
        List<RestauranteReviewDTO> dtos = new ArrayList<>();

        for (Object[] result : results) {
            RestauranteReviewDTO dto = RestauranteUfDTOMapper.map(result);
            dtos.add(dto);
        }

        return dtos;
    }

    @Query(value = "SELECT r.nome, r.id, (SELECT nome_foto FROM restaurante_foto WHERE fk_restaurante = r.id LIMIT 1) AS nomeFoto " +
            " from restaurante as r where especialidade != ?1 LIMIT 15", nativeQuery = true)
    List<Object[]> findByRestauranteEspecialidade(String especialidade);
    default List<RestauranteReviewDTO> findByRestauranteEspecialidadeDTO(String especialidade) {
        List<Object[]> results = findByRestauranteEspecialidade(especialidade);
        List<RestauranteReviewDTO> dtos = new ArrayList<>();

        for (Object[] result : results) {
            RestauranteReviewDTO dto = RestauranteUfDTOMapper.map(result);
            dtos.add(dto);
        }
        return dtos;
    }
    List<Restaurante> findByBeneficio(boolean beneficio);

    @Query(value = "SELECT r.nome, r.id, (SELECT nome_foto FROM restaurante_foto WHERE fk_restaurante = r.id LIMIT 1) AS nomeFoto, (SELECT SUM(nt_comida + nt_ambiente + nt_atendimento) / (SELECT COUNT(fk_restaurante) * 3 FROM review WHERE fk_restaurante = r.id)FROM review WHERE fk_restaurante = r.id) AS media FROM restaurante AS r JOIN restaurante_foto AS rf ON r.id = rf.fk_restaurante ORDER BY media DESC LIMIT 15", nativeQuery = true)
    List<Object[]> findByRestauranteBemAvaliado();

// select pulando linha para entender ->   SELECT r.nome, r.id, (SELECT nome_foto FROM restaurante_foto WHERE fk_restaurante = r.id LIMIT 1) AS nomeFoto,
//            (SELECT SUM(nt_comida + nt_ambiente + nt_atendimento) / (SELECT COUNT(fk_restaurante) * 3 FROM review WHERE fk_restaurante = r.id)
//    FROM review WHERE fk_restaurante = r.id) AS media
//    FROM restaurante AS r JOIN restaurante_foto AS rf ON r.id = rf.fk_restaurante
//    ORDER BY media DESC LIMIT 15;
    default List<RestauranteReviewDTO> findByRestauranteBemAvaliadoDTO() {
        List<Object[]> results = findByRestauranteBemAvaliado();
        List<RestauranteReviewDTO> dtos = new ArrayList<>();

        for (Object[] result : results) {
            RestauranteReviewDTO dto = RestauranteReviewDTOMapper.map(result);
            dtos.add(dto);
        }

        return dtos;
    }

}
