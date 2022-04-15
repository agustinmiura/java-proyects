package ar.com.miura.usersapi.dto;

@lombok.Data
@lombok.AllArgsConstructor
public class UserInputDto {


    @javax.validation.constraints.Size(min = 1, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @javax.validation.constraints.Size(min = 1, max = 50, message = "Full must be between 2 and 50 characters")
    private String fullName;

    @javax.validation.constraints.NotEmpty
    private String role;

    @javax.validation.constraints.Email(message = "Email must be valid")
    private String emailAddress;

    @javax.validation.constraints.NotEmpty
    private String status;

}
