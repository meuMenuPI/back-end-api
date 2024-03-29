package meumenu.application.meumenu.review;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record DadosCadastroReview(
        @NotNull
        int fkRestaurante,
        @NotNull
        int fkUsuario,
        @NotBlank
        @Length(max = 200)
        String descricao,
        @NotNull
        Double nt_comida,
        @NotNull
        Double nt_ambiente,
        @NotNull
        Double nt_atendimento

)
        {
}
