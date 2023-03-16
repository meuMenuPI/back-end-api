package meumenu.application.meumenu.usuario;

import jakarta.persistence.*;
import lombok.*;
import meumenu.application.meumenu.interfaces.Recomendavel;
import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.restaurante.RestauranteDTO;

import java.util.ArrayList;
import java.util.List;


@Table(name = "usuario")
@Entity(name = "Usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class  Usuario implements Recomendavel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String email;
    private String senha;
    @Enumerated(EnumType.STRING)
    private TipoComidaPreferida tipoComidaPreferida;


    public Usuario(DadosCadastroUsuario dados) {

        this.nome = dados.nome();
        this.sobrenome = dados.sobrenome();
        this.cpf = dados.cpf();
        this.email = dados.email();
        this.senha = dados.senha();
        this.tipoComidaPreferida = dados.tipoComidaPreferida();
    }

    @Override
    public List recomendar(List<Usuario> listaUsuario, List<Restaurante> listaRestaurante, int id) {
        List<RestauranteDTO> listaRecomendacao = new ArrayList<>();
        Usuario tempU = listaUsuario.get(0);
        for(Usuario u : listaUsuario){
            if(u.getId() == id){
                tempU = u;
            }
        }
        for(Restaurante r : listaRestaurante){
            if(r.getEspecialidade().name().equals(tempU.getTipoComidaPreferida().name())){
                listaRecomendacao.add(new RestauranteDTO(r.getId(),r.getNome(), r.getEspecialidade().name(), r.getTelefone(), r.getSite(), r.getEstrela()));
            }
        }
        return listaRecomendacao;
    }
}