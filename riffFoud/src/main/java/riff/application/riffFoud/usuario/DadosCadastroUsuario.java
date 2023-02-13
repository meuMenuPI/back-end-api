package riff.application.riffFoud.usuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import riff.application.riffFoud.endereco.DadosEndereco;

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
