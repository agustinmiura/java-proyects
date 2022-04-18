package ar.com.miura.usersapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class LoginInputDto implements Serializable {

    @Size(min = 1, max = 50, message = "Full must be between 2 and 50 characters")
    private String user;

    @Size(min = 1, max = 50, message = "Full must be between 2 and 50 characters")
    private String password;

}
