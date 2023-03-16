package meumenu.application.meumenu.restaurante;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestauranteDTO {

    private Integer id;
    private String nome;
    private String especialidade;
    private String telefone;
    private String site;
    private Integer estrela;


}
