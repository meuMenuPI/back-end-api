package meumenu.application.meumenu.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import meumenu.application.meumenu.favorito.FavoritoId;

import java.io.Serializable;
import java.util.Objects;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewId implements Serializable {

    Integer fk_usuario;
    Integer fk_restaurante;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewId reviewId = (ReviewId) o;
        return Objects.equals(fk_usuario, reviewId.fk_usuario) && Objects.equals(fk_restaurante, reviewId.fk_restaurante);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fk_usuario, fk_restaurante);
    }
}
