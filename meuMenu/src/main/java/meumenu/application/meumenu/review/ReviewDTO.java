package meumenu.application.meumenu.review;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ReviewDTO {
    private String nome;
    private int fkRestaurante;
    private int fkUsuario;
    private String data_hora;
    private String descricao;
    private Double nt_comida;
    private Double nt_ambiente;
    private Double nt_atendimento;

}
