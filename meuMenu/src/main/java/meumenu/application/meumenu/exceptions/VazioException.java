package meumenu.application.meumenu.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class VazioException extends RuntimeException{

    public VazioException(String message) {
        super(message);
    }
}







