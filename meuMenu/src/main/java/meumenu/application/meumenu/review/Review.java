package meumenu.application.meumenu.review;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "review")
@Entity(name = "Review")
@Data
@NoArgsConstructor
@IdClass(ReviewId.class)
public class Review {
    @Id
    private int fkRestaurante;
    @Id
    private int fkUsuario;
    @Id
    private LocalDateTime data_hora;
    private String descricao;
    private Double nt_comida;
    private Double nt_ambiente;
    private Double nt_atendimento;

    public Review(DadosCadastroReview dados) {

        LocalDateTime data = LocalDateTime.now();
        this.fkRestaurante = dados.fkRestaurante();
        this.fkUsuario = dados.fkUsuario();
        this.data_hora = data;
        this.descricao = dados.descricao();
        this.nt_comida = dados.nt_comida();
        this.nt_ambiente = dados.nt_ambiente();
        this.nt_atendimento = dados.nt_atendimento();
    }
}
