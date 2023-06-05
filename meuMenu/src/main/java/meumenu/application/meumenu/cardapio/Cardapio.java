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

    private String descricao;

    private String fotoPrato;

    public Cardapio(DadosCadastroCardapio dados) {
        this.fk_restaurante = dados.fk_restaurante();
        this.nome = dados.nome();
        this.preco = dados.preco();
        this.estiloGastronomico = dados.estiloGastronomico();
        this.descricao = dados.descricao();
        this.fotoPrato = null;
    }

    public Cardapio(Integer id, Integer fk_restaurante, String nome, Double preco, Especialidade estiloGastronomico, String descricao) {
        this.id = id;
        this.fk_restaurante = fk_restaurante;
        this.nome = nome;
        this.preco = preco;
        this.estiloGastronomico = estiloGastronomico;
        this.descricao = descricao;
        this.fotoPrato = null;
    }
}
