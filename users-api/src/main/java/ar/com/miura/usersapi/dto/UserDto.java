package ar.com.miura.usersapi.dto;

/*
* The USER entity will have the following columns:
*   “username”,
*   “fullName”,
*   “role”,
*   “emailAddress”, and
*   “status” (status will be either “pending” or “active”)
* */
@lombok.Data
@lombok.AllArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String fullName;
    private String role;
    private String emailAddress;
    private String status;
}

