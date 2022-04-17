package ar.com.miura.usersapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
public class UserInputDto {


    @Size(min = 1, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @Size(min = 1, max = 50, message = "Full must be between 2 and 50 characters")
    private String fullName;

    @NotEmpty
    private List<String> roles;

    @Email(message = "Email must be valid")
    private String emailAddress;

    @NotEmpty
    private String status;

}
