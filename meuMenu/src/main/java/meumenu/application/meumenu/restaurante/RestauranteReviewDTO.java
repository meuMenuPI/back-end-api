package meumenu.application.meumenu.restaurante;

import jakarta.persistence.*;
import lombok.Data;


@Data
public class RestauranteReviewDTO {
    private String nome;
    private Integer id;
    private String nomeFoto;
    private Double media;



    public RestauranteReviewDTO(String nome,Integer id,String nomeFoto, Double media) {
        this.nome = nome;
        this.id = id;
        this.nomeFoto = nomeFoto;
        this.media = media;

    }

    public RestauranteReviewDTO(String nome,Integer id,String nomeFoto) {
        this.nome = nome;
        this.id = id;
        this.nomeFoto = nomeFoto;
    }
}
