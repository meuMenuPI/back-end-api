package meumenu.application.meumenu.usuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import meumenu.application.meumenu.endereco.DadosEndereco;

public record DadosCadastroUsuario (
        //validação atraves do "validation" metodo do spring
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email,
        @NotBlank
                @Pattern(regexp = "\\d{11,15}")
        String cpf,
        @NotNull
        GostoCulinario  gostoCulinario,
        @NotNull
        @Valid
        DadosEndereco endereco){
}
