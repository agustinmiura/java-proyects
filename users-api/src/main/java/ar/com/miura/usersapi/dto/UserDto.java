package ar.com.miura.usersapi.dto;

import ar.com.miura.usersapi.entity.User;


@lombok.Data
@lombok.AllArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String fullName;
    private String role;
    private String emailAddress;
    private String status;

    /**
     * @param user
     * @return
     */
    public static UserDto fromEntity(User user) {
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getRole(),
            user.getEmailAddress(),
            user.getStatus()
        );
    }

}

