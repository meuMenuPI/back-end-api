package meumenu.application.meumenu.interfaces;

import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.usuario.Usuario;

import java.util.List;

public interface Recomendavel {
    public List recomendar(List<Usuario> listaUsuario, List<Restaurante> listaRestaurante, int id);
}
