package meumenu.application.meumenu.favorito;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(FavoritoId.class)
public class Favorito{
    @Id
    @NotNull
    private Integer fk_usuario;
    @Id
    @NotNull
    private Integer fk_restaurante;

}

