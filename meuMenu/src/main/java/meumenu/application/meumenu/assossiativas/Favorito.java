package meumenu.application.meumenu.assossiativas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

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

