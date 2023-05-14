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
        @NotBlank
        String descricao,
        @NotNull
        Double preco,
        @NotNull
        Especialidade estiloGastronomico,

        @NotNull
        String qtd_carboidratos,

        @NotNull
        String qtd_proteinas,

        @NotNull
        String qtd_acucar,

        @NotNull
        String qtd_calorias,

        @NotNull
        String qtd_gorduras_totais

) {

}
