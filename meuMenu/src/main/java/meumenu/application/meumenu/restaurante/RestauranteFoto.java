package meumenu.application.meumenu.restaurante;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name = "restauranteFoto")
@Entity(name = "RestauranteFoto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class RestauranteFoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomeFoto;
    private boolean fachada;
    private boolean interior;

    public RestauranteFoto(String nomeFoto, boolean fachada, boolean interior) {
        this.nomeFoto = nomeFoto;
        this.fachada = fachada;
        this.interior = interior;
    }
}
