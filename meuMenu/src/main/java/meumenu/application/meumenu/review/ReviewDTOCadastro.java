package meumenu.application.meumenu.review;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReviewDTOCadastro{
    private int fkRestaurante;
    private int fkUsuario;
    private String descricao;
    private Double nt_comida;
    private Double nt_ambiente;
    private Double nt_atendimento;
}
