package meumenu.application.meumenu.restaurante;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name = "RestauranteFoto")
@Entity(name = "RestauranteFoto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteFoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int fkRestaurante;
    private String nomeFoto;
    private boolean fachada;
    private boolean interior;

    public RestauranteFoto(int fkRestaurante, String nomeFoto, boolean fachada, boolean interior) {
        this.fkRestaurante = fkRestaurante;
        this.nomeFoto = nomeFoto;
        this.fachada = fachada;
        this.interior = interior;
    }
}
