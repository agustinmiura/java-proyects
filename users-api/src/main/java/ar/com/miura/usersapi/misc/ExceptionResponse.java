package ar.com.miura.usersapi.misc;

@lombok.AllArgsConstructor
@lombok.Data
public class ExceptionResponse {
    private java.time.LocalDateTime timestamp;
    private String message;
    private String details;
}
