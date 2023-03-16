package meumenu.application.meumenu.usuario;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDTO {
    private Integer id;
    private String nome;
    private String sobrenome;
    private String email;
    private String tipoComidaFavorita;
}

