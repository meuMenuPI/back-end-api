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
@IdClass(ReviewId.class)
public class Review {
    @Id
    private int fk_restaurante;
    @Id
    private int fk_usuario;
    @Id
    private LocalDateTime data_hora;
    private String descricao;
    private Double nt_comida;
    private Double nt_ambiente;
    private Double nt_atendimento;

    public Review(DadosCadastroReview dados) {

        LocalDateTime data = LocalDateTime.now();
        this.fk_restaurante = dados.fk_restaurante();
        this.fk_usuario = dados.fk_usuario();
        this.data_hora = data;
        this.descricao = dados.descricao();
        this.nt_comida = dados.nt_comida();
        this.nt_ambiente = dados.nt_ambiente();
        this.nt_atendimento = dados.nt_atendimento();
    }
}
