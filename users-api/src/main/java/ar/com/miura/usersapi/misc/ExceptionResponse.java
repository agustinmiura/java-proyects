package ar.com.miura.usersapi.misc;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExceptionResponse {
    private java.time.LocalDateTime timestamp;
    private String message;
    private String details;
}
