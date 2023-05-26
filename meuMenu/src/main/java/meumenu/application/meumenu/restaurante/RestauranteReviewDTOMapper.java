package meumenu.application.meumenu.restaurante;

public class RestauranteReviewDTOMapper {

    public static RestauranteReviewDTO map(Object[] result) {
        String nome = (String) result[0];
        Integer id = (Integer) result[1];
        String nomeFoto = (String) result[2];
        Double media = (Double) result[3];

        return new RestauranteReviewDTO(nome, id, nomeFoto, media);
    }

}
