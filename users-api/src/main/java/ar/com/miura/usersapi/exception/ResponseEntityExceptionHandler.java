package ar.com.miura.usersapi.exception;

@lombok.extern.slf4j.Slf4j
@org.springframework.web.bind.annotation.ControllerAdvice
@org.springframework.web.bind.annotation.RestController
public class ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public org.springframework.http.ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
        log.error("Error: {} , stackTrace : {} ", ex.getMessage(), ex.getStackTrace());
        ar.com.miura.usersapi.misc.ExceptionResponse exceptionResponse = new ar.com.miura.usersapi.misc.ExceptionResponse(
                java.time.LocalDateTime.now(),
                ex.getMessage(),
                "/details"
        );
        return new org.springframework.http.ResponseEntity(exceptionResponse, org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<Object> handleException(Exception ex) {
        log.error("Error: {} , stackTrace : {} ", ex.getMessage(), ex.getStackTrace());
        ar.com.miura.usersapi.misc.ExceptionResponse exceptionResponse = new ar.com.miura.usersapi.misc.ExceptionResponse(
                java.time.LocalDateTime.now(),
                ex.getMessage(),
                "/details"
        );
        return new org.springframework.http.ResponseEntity(exceptionResponse, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<Object> handleMethodArgumentNotValid(
        org.springframework.web.bind.MethodArgumentNotValidException ex) {
        log.error("Error: {} , stackTrace : {} ", ex.getMessage(), ex.getStackTrace());
        ar.com.miura.usersapi.misc.ExceptionResponse exceptionResponse = new ar.com.miura.usersapi.misc.ExceptionResponse(
                java.time.LocalDateTime.now(),
                "Validation failed",
                ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()
        );
        return new org.springframework.http.ResponseEntity(exceptionResponse, org.springframework.http.HttpStatus.BAD_REQUEST);
    }

}
