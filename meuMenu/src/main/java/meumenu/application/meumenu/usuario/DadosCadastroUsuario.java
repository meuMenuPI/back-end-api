package meumenu.application.meumenu.usuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import meumenu.application.meumenu.enums.Especialidade;
import org.hibernate.validator.constraints.br.CPF;

public record DadosCadastroUsuario(
        //validação atraves do "validation" metodo do spring
        @NotBlank
        String nome,
        @NotBlank
        String sobrenome,
        @NotBlank
        @Pattern(
                regexp = "([0-9]{3}[.]?[0-9]{3}[.]?[0-9]{3}-[0-9]{2})|([0-9]{11})"
        )
        String cpf,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String senha,

        @NotNull
        Especialidade tipoComidaPreferida,

        String fotoPerfil,

        String codEmail)           {
}
