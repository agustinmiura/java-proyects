package ar.com.miura.usersapi.service;

import ar.com.miura.usersapi.dto.UserDto;
import ar.com.miura.usersapi.dto.UserInputDto;
import ar.com.miura.usersapi.entity.User;
import ar.com.miura.usersapi.exception.UserNotFoundException;
import ar.com.miura.usersapi.repository.UserRepository;
import ar.com.miura.usersapi.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Component
public class UserService {
    private static List<UserDto> users;

    private IdGenerator idGenerator;
    private UserRepository userRepository;

    static {
        users = new ArrayList<>();
    }

    @Autowired
    public UserService(IdGenerator idGenerator, UserRepository userRepository) {
        this.idGenerator = idGenerator;
        this.userRepository = userRepository;
    }

    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDto::fromEntity).collect(toList());
    }

    public UserDto findOne(String id) {
        Optional<User> found = userRepository.findById(id);
        if (found.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return UserDto.fromEntity(found.get());
    }

    public UserDto save(UserInputDto userDto) {
        String id = idGenerator.generateId();
        User saved = userRepository.save(new User(
            id,
            userDto.getUsername(),
            userDto.getFullName(),
            userDto.getRole(),
            userDto.getEmailAddress(),
            userDto.getStatus(),
            LocalDateTime.now(),
            null
        ));
        return UserDto.fromEntity(saved);
    }

    public void delete(String id) {
        Optional<User> found = userRepository.findById(id);
        if (found.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        userRepository.delete(found.get());
    }


}
