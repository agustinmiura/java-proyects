package ar.com.miura.usersapi.exception;

import ar.com.miura.usersapi.misc.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RestController
public class ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleInvalidEmail(AuthenticationException ex) {
        log.error(" Error ", ex);
        ExceptionResponse exceptionResponse = new ar.com.miura.usersapi.misc.ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                "/details"
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidEmailAddress.class)
    public ResponseEntity<Object> handleInvalidEmail(InvalidEmailAddress ex) {
        log.error(" Error ", ex);
        ExceptionResponse exceptionResponse = new ar.com.miura.usersapi.misc.ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                "/details"
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(UserNotFoundException ex) {
        log.error(" Error ", ex);
        ExceptionResponse exceptionResponse = new ar.com.miura.usersapi.misc.ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                "/details"
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFound(RoleNotFoundException ex) {
        log.error(" Error ", ex);
        ExceptionResponse exceptionResponse = new ar.com.miura.usersapi.misc.ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                "/details"
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error(" Error ", ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                "/details"
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(Exception ex) {
        log.error(" Error ", ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                "/details"
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex) {
        log.error(" Error ", ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                java.time.LocalDateTime.now(),
                "Validation failed",
                ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()
        );
        return new ResponseEntity(exceptionResponse, org.springframework.http.HttpStatus.BAD_REQUEST);
    }

}
