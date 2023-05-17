package meumenu.application.meumenu.endereco;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name = "endereco")
@Entity(name = "Endereco")
@EqualsAndHashCode(of = "id")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer fkRestaurante;
    private String complemento;
    private Integer numero;
    private String cep;

    public Endereco(DadosCadastroEndereco dados) {
        this.id = dados.id();
        this.fkRestaurante = dados.fkRestaurante();
        this.complemento = dados.complemento();
        this.numero = dados.numero();
        this.cep = dados.cep();
    }
}
