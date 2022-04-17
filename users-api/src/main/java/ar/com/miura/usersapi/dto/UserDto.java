package ar.com.miura.usersapi.dto;

import ar.com.miura.usersapi.entity.Role;
import ar.com.miura.usersapi.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@lombok.Data
@lombok.AllArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String fullName;
    private Set<String> roles = new HashSet<>();
    private String emailAddress;
    private String status;

    /**
     * @param user
     * @return
     */
    public static UserDto fromEntity(User user) {

        Set<Role> roles = user.getRoles();
        Set<String> strings = roles.stream().map(Role::getName).collect(Collectors.toSet());

        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            strings,
            user.getEmailAddress(),
            user.getStatus()
        );
    }

}

