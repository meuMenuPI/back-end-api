package meumenu.application.meumenu.cardapio;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import meumenu.application.meumenu.enums.Especialidade;
import meumenu.application.meumenu.restaurante.Restaurante;

@Table(name = "cardapio")
@Entity(name = "Cardapio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cardapio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer fk_restaurante;
    private String nome;
    private Double preco;
    @Enumerated(EnumType.STRING)
    private Especialidade estiloGastronomico;

    public Cardapio(DadosCadastroCardapio dados) {
        this.fk_restaurante = dados.fk_restaurante();
        this.nome = dados.nome();
        this.preco = dados.preco();
        this.estiloGastronomico = dados.estiloGastronomico();

    }
}
