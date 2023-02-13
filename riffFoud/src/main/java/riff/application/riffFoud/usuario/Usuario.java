package riff.application.riffFoud.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import riff.application.riffFoud.endereco.Endereco;


@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class  Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String email;
    private String cpf;

    @Enumerated(EnumType.STRING)
    private GostoCulinario gostoCulinario;

    @Embedded
    private Endereco endereco;

    public Usuario(DadosCadastroUsuario dados) {

        this.nome = dados.nome();
        this.email = dados.email();
        this.cpf = dados.cpf();
        this.gostoCulinario = dados.gostoCulinario();
        this.endereco = new Endereco(dados.endereco());
    }
}