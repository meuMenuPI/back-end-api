package meumenu.application.meumenu.usuario;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UsuarioDTO {
    private String nome;
    private String sobrenome;
    private String email;
    private String tipoComidaFavorita;

    public UsuarioDTO(String nome, String sobrenome, String email, String tipoComidaFavorita) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.tipoComidaFavorita = tipoComidaFavorita;
    }

}
