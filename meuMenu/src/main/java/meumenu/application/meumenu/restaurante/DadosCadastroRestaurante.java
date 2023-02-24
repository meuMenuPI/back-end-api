package meumenu.application.meumenu.restaurante;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import meumenu.application.meumenu.usuario.TipoComidaPreferida;

public record DadosCadastroRestaurante (
        //validação atraves do "validation" metodo do spring
        @NotNull
        int usuario,
        @NotBlank
        String nome,
        @NotBlank
        @Pattern(regexp = "\\d{14,16}")
        String cnpj,
        @NotNull
        Especialidade especialidade,
        @NotNull
        String telefone,

        String site,
        int estrela

        ){
}
