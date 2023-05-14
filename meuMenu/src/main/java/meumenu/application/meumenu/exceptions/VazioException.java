package meumenu.application.meumenu.exceptions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import meumenu.application.meumenu.assossiativas.FavoritoRepository;
import meumenu.application.meumenu.restaurante.*;
import meumenu.application.meumenu.services.RestauranteService;
import meumenu.application.meumenu.usuario.UsuarioDTO;
import meumenu.application.meumenu.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class VazioException extends RuntimeException{

    public VazioException(String message) {
        super(message);
    }
}







