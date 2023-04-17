package meumenu.application.meumenu.restaurante;

import jakarta.persistence.*;
import lombok.*;
import meumenu.application.meumenu.enums.Especialidade;
import meumenu.application.meumenu.interfaces.Recomendavel;
import meumenu.application.meumenu.usuario.Usuario;
import meumenu.application.meumenu.usuario.UsuarioDTO;

import java.util.ArrayList;
import java.util.List;

@Table(name = "restaurante")
@Entity(name = "Restaurante")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Restaurante implements Recomendavel {
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
    private Integer estrela;

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
    public List recomendar(List<Usuario> listaUsuario, List<Restaurante> listaRestaurante, int id) {
        List<UsuarioDTO> listaRecomendacao = new ArrayList<>();

        Restaurante tempR = listaRestaurante.get(0);
        for(Restaurante r : listaRestaurante){
            if(r.getId() == id){
                tempR = r;
            }
        }
        for(Usuario u : listaUsuario){
            if(u.getTipoComidaPreferida().name().equals(tempR.getEspecialidade().name())){
                listaRecomendacao.add(new UsuarioDTO(u.getId(),u.getNome(), u.getSobrenome(), u.getEmail(), u.getTipoComidaPreferida().name()));
            }
        }
        return listaRecomendacao;
    }
}
