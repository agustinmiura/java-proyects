package ar.com.miura.usersapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidEmailAddress extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InvalidEmailAddress(String message) {
        super(message);
    }
}
