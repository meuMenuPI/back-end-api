package meumenu.application.meumenu.usuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroUsuario (
        //validação atraves do "validation" metodo do spring
        @NotBlank
        String nome,
        @NotBlank
        String sobrenome,
        @NotBlank
        @Pattern(regexp = "\\d{11,15}")
        String cpf,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String senha,

        @NotNull
        TipoComidaPreferida tipoComidaPreferida){
}
