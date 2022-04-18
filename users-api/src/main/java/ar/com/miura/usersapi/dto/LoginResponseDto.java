package ar.com.miura.usersapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
public class LoginResponseDto implements Serializable {
    boolean success;
    String jwtToken;
}
