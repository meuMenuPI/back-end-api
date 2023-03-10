package meumenu.application.meumenu.restaurante;

import jakarta.persistence.*;
import lombok.*;
import meumenu.application.meumenu.interfaces.ClientesInterface;
import meumenu.application.meumenu.usuario.DadosCadastroUsuario;
import meumenu.application.meumenu.usuario.TipoComidaPreferida;
import meumenu.application.meumenu.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;

@Table(name = "restaurante")
@Entity(name = "Restaurante")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Restaurante implements ClientesInterface {
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

    @Override
    public List recomendar(List<Usuario> lu, List<Restaurante> lr, int id) {
        List<Usuario> listaRecomendacao = new ArrayList<>();

        Restaurante tempR = lr.get(0);
        for(Restaurante r : lr){
            if(r.getId() == id){
                tempR = r;
            }
        }
        for(Usuario u : lu){
            if(u.getTipoComidaPreferida().name().equals(tempR.getEspecialidade().name())){
                listaRecomendacao.add(u);
            }
        }
        return listaRecomendacao;
    }
}
