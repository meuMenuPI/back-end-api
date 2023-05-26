package meumenu.application.meumenu.services.builder;


import meumenu.application.meumenu.restaurante.DadosCadastroRestaurante;
import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.restaurante.RestauranteDTO;

import java.util.List;

import static meumenu.application.meumenu.enums.Especialidade.*;

public class RestauranteBuilder {

    private RestauranteBuilder(){
        throw new IllegalStateException("Classe utilitária");
    }
    public static Restaurante criarRestaurante(){
        return new Restaurante(new DadosCadastroRestaurante(1,"Kauan","28535193000102",BRASILEIRA,true,"11962623116","teste.com",2));
    }
    public static DadosCadastroRestaurante criarDadosCadastroRestaurante(){
        return new DadosCadastroRestaurante(1,"Kauan","28535193000102",BRASILEIRA,true,"11962623116","teste.com",2);
    }

    public static RestauranteDTO criarRestauranteDTO(){
        return new RestauranteDTO("KauansBar","BRASILEIRA",true,"11962623116","teste1.com",2);
    }

    public static List<RestauranteDTO> criarListaDto(){
        return List.of(
                new RestauranteDTO("LucasBar","BRASILEIRA",true,"11962623116","teste4.com",2),
                new RestauranteDTO("BrunoBar","MEXICANA",true,"1196268271","teste2.com",1),
                new RestauranteDTO("KauansBar2","JAPONESA",true,"1196269372","teste3.com",0)
        );
    }



}
