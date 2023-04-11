package meumenu.application.meumenu.assossiativas;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FavoritoId implements Serializable {

    Integer fk_usuario;
    Integer fk_restaurante;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoritoId that = (FavoritoId) o;
        return Objects.equals(fk_usuario, that.fk_usuario) && Objects.equals(fk_restaurante, that.fk_restaurante);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fk_usuario, fk_restaurante);
    }
}
