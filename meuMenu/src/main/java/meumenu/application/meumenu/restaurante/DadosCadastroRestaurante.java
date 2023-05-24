package meumenu.application.meumenu.restaurante;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import meumenu.application.meumenu.enums.Especialidade;
import org.hibernate.validator.constraints.br.CNPJ;

public record DadosCadastroRestaurante (
        //validação atraves do "validation" metodo do spring
        @NotNull
        int usuario,
        @NotBlank
        String nome,
        @NotBlank
        @Pattern(
                regexp = "([0-9]{2}[.]?[0-9]{3}[.]?[0-9]{3}[/]?[0-9]{4}[-]?[0-9]{2})"
        )
        String cnpj,
        @NotNull
        Especialidade especialidade,
        boolean beneficio,
        @NotNull
        String telefone,

        String site,
        Integer estrela

        ){
}
