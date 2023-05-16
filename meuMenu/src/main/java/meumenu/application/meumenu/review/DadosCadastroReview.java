package meumenu.application.meumenu.review;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record DadosCadastroReview(
        @NotNull
        int fk_restaurante,
        @NotNull
        int fk_usuario,
        @NotBlank
        LocalDateTime dataHora,
        @NotBlank
        @Length(max = 200)
        String descricao,
        @NotBlank
        Double nt_comida,
        @NotBlank
        Double nt_ambiente,
        @NotBlank
        Double nt_atendimento

)
        {
}
