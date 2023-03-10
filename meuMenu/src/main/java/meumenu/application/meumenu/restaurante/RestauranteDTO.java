package meumenu.application.meumenu.restaurante;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class RestauranteDTO {
    private String nome;
    private String especialidade;
    private String telefone;
    private String site;
    private int estrela;

    public RestauranteDTO( String nome, String especialidade, String telefone, String site, int estrela) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.telefone = telefone;
        this.site = site;
        this.estrela = estrela;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getEstrela() {
        return estrela;
    }

    public void setEstrela(int estrela) {
        this.estrela = estrela;
    }
}
