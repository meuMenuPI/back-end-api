package meumenu.application.meumenu.interfaces;

import meumenu.application.meumenu.restaurante.Restaurante;
import meumenu.application.meumenu.usuario.Usuario;

import java.util.List;

public interface ClientesInterface {
    public List recomendar(List<Usuario> lu, List<Restaurante> lr, int id);
}
