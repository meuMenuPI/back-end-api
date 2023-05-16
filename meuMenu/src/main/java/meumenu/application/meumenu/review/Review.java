package meumenu.application.meumenu.review;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import meumenu.application.meumenu.favorito.FavoritoId;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "review")
@Entity(name = "Review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ReviewId.class)
public class Review {
    @Id
    private int fk_restaurante;
    @Id
    private int fk_usuario;
    private LocalDate dataHora;
    private String descricao;
    private Double nt_comida;
    private Double nt_ambiente;
    private Double nt_atendimento;

}
