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
    private Integer fk_restaurante;
    private Integer fk_usuario;
    private String complemento;
    private Integer numero;
    private String cep;
    private String uf;

    public Endereco(DadosCadastroEndereco dados) {
        this.fk_restaurante = dados.fk_restaurante();
        this.fk_usuario = dados.fk_usuario();
        this.complemento = dados.complemento();
        this.numero = dados.numero();
        this.cep = dados.cep();
        this.uf = dados.uf();
    }
}
