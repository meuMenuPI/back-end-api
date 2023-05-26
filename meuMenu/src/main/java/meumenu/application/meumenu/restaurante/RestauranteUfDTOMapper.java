package meumenu.application.meumenu.restaurante;

public class RestauranteUfDTOMapper {

    public static RestauranteReviewDTO map(Object[] result) {
        String nome = (String) result[0];
        Integer id = (Integer) result[1];
        String nomeFoto = (String) result[2];

        return new RestauranteReviewDTO(nome, id, nomeFoto);
    }

}
