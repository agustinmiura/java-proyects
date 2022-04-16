package ar.com.miura.usersapi.exception;

import ar.com.miura.usersapi.misc.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
@RestController
public class ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
        log.error("Error: {} , stackTrace : {} ", ex.getMessage(), ex.getStackTrace());
        ExceptionResponse exceptionResponse = new ar.com.miura.usersapi.misc.ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                "/details"
        );
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("Error: {} , stackTrace : {} ", ex.getMessage(), ex.getStackTrace());
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
        log.error("Error: {} , stackTrace : {} ", ex.getMessage(), ex.getStackTrace());
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                java.time.LocalDateTime.now(),
                "Validation failed",
                ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()
        );
        return new ResponseEntity(exceptionResponse, org.springframework.http.HttpStatus.BAD_REQUEST);
    }

}
