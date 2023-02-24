package meumenu.application.meumenu.restaurante;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import meumenu.application.meumenu.usuario.DadosCadastroUsuario;
import meumenu.application.meumenu.usuario.TipoComidaPreferida;

@Table(name = "restaurante")
@Entity(name = "Restaurante")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int usuario;
    private String nome;
    private String cnpj;
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;
    private String telefone;
    private String site;
    private int estrela;

    public Restaurante(DadosCadastroRestaurante dados) {
        this.usuario = dados.usuario();
        this.nome = dados.nome();
        this.cnpj = dados.cnpj();
        this.especialidade = dados.especialidade();
        this.telefone = dados.telefone();
        this.site = dados.site();
        this.estrela = dados.estrela();

    }
}
