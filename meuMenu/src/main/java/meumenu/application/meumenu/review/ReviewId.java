package meumenu.application.meumenu.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import meumenu.application.meumenu.favorito.FavoritoId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewId implements Serializable {

    Integer fkUsuario;
    Integer fkRestaurante;
    LocalDateTime data_hora;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewId reviewId = (ReviewId) o;
        return Objects.equals(fkUsuario, reviewId.fkUsuario) && Objects.equals(fkRestaurante, reviewId.fkRestaurante) && Objects.equals(data_hora, reviewId.data_hora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fkUsuario, fkRestaurante, data_hora);
    }
}
