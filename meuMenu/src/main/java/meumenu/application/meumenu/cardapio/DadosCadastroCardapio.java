package meumenu.application.meumenu.cardapio;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import meumenu.application.meumenu.enums.Especialidade;

public record DadosCadastroCardapio(
        @NotNull
        Integer fk_restaurante,
        @NotBlank
        String nome,
        @NotNull
        Double preco,
        @NotNull
        Especialidade estiloGastronomico

) {

}
