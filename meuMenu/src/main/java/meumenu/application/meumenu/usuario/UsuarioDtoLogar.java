package meumenu.application.meumenu.usuario;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDtoLogar {
    private String email;
    private String senha;
}

