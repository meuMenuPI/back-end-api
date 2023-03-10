package meumenu.application.meumenu.usuario;

import jakarta.persistence.*;
import lombok.*;
import meumenu.application.meumenu.interfaces.ClientesInterface;
import meumenu.application.meumenu.restaurante.Restaurante;
import org.springframework.beans.factory.annotation.Autowired;
import meumenu.application.meumenu.usuario.DadosCadastroUsuario;
import meumenu.application.meumenu.usuario.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;


@Table(name = "usuario")
@Entity(name = "Usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class  Usuario implements ClientesInterface {
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
    public List recomendar(List<Usuario> lu, List<Restaurante> lr, int id) {
        List<Restaurante> listaRecomendacao = new ArrayList<>();

        Usuario tempU = lu.get(0);
        for(Usuario u : lu){
            if(u.getId() == id){
                tempU = u;
            }
        }
        for(Restaurante r : lr){
            if(r.getEspecialidade().name().equals(tempU.getTipoComidaPreferida().name())){
                listaRecomendacao.add(r);
            }
        }
        return listaRecomendacao;
    }
}