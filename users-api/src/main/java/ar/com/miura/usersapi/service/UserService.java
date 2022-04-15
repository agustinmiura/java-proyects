package ar.com.miura.usersapi.service;

@org.springframework.stereotype.Component
public class UserService {
    private static java.util.List<ar.com.miura.usersapi.dto.UserDto> users;

    private ar.com.miura.usersapi.utils.IdGenerator idGenerator;

    static {
        users = new java.util.ArrayList<>();
    }

    @org.springframework.beans.factory.annotation.Autowired
    public UserService(ar.com.miura.usersapi.utils.IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        users.add(new ar.com.miura.usersapi.dto.UserDto(
                "id",
                "username",
                "fullName",
                "role",
                "emailAddress",
                "status"
        ));
    }

    public java.util.List<ar.com.miura.usersapi.dto.UserDto> findAll() {
        return users;
    }

    public ar.com.miura.usersapi.dto.UserDto findOne(String id) {
        java.util.Optional<ar.com.miura.usersapi.dto.UserDto> found = users.stream().filter(u -> u.getId().equals(id)).findFirst();
        if (found.isEmpty()) {
            throw new ar.com.miura.usersapi.exception.UserNotFoundException(id);
        }
        return found.get();
    }

    public ar.com.miura.usersapi.dto.UserDto save(ar.com.miura.usersapi.dto.UserInputDto userDto) {
        String id = idGenerator.generateId();
        users.add(new ar.com.miura.usersapi.dto.UserDto(
                id,
                userDto.getUsername(),
                userDto.getFullName(),
                userDto.getRole(),
                userDto.getEmailAddress(),
                userDto.getStatus()
        ));
        return new ar.com.miura.usersapi.dto.UserDto(
                id,
                userDto.getUsername(),
                userDto.getFullName(),
                userDto.getRole(),
                userDto.getEmailAddress(),
                userDto.getStatus()
        );
    }

    public void delete(String id) {
        java.util.Optional<ar.com.miura.usersapi.dto.UserDto> found = users.stream().filter(u -> u.getId().equals(id)).findFirst();
        if (found.isEmpty()) {
            throw new ar.com.miura.usersapi.exception.UserNotFoundException(id);
        }
        users = users.stream().filter( u -> !u.getId().equals(id) ).collect(java.util.stream.Collectors.toList());
    }


}
