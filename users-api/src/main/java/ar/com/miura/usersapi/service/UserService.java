package ar.com.miura.usersapi.service;

import ar.com.miura.usersapi.dto.UserDto;
import ar.com.miura.usersapi.dto.UserInputDto;
import ar.com.miura.usersapi.entity.Role;
import ar.com.miura.usersapi.entity.User;
import ar.com.miura.usersapi.exception.RoleNotFoundException;
import ar.com.miura.usersapi.exception.UserNotFoundException;
import ar.com.miura.usersapi.repository.RoleRepository;
import ar.com.miura.usersapi.repository.UserRepository;
import ar.com.miura.usersapi.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;


@Component
public class UserService {
    private static List<UserDto> users;

    private IdGenerator idGenerator;
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    static {
        users = new ArrayList<>();
    }

    @Autowired
    public UserService(IdGenerator idGenerator, UserRepository userRepository, RoleRepository roleRepository) {
        this.idGenerator = idGenerator;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Cacheable(value="users", key="#pageRequest.pageNumber")
    public List<UserDto> findAll(PageRequest pageRequest) {
        Iterable<User> users = userRepository.findAll(pageRequest);
        List<UserDto> userList = new ArrayList();
        users.forEach( user -> userList.add(UserDto.fromEntity(user)));
        return userList;
    }

    @Cacheable(value="user", key="#id")
    public UserDto findOne(String id) {
        Optional<User> found = userRepository.findById(id);
        if (found.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return UserDto.fromEntity(found.get());
    }

    public UserDto save(UserInputDto inputDto) {
        String id = idGenerator.generateId();

        Optional<String> invalid = inputDto.getRoles().stream().filter(role -> roleRepository.findByName(role)==null).findFirst();
        if (!invalid.isEmpty()) {
            throw new RoleNotFoundException(invalid.get());
        }
        Set<Role> roles =  inputDto.getRoles().stream().map( name -> roleRepository.findByName(name)).collect(Collectors.toSet());

        User saved = userRepository.save(new User(
            id,
            inputDto.getUsername(),
            inputDto.getFullName(),
            roles,
            inputDto.getEmailAddress(),
            inputDto.getStatus(),
            LocalDateTime.now(),
            null
        ));
        return UserDto.fromEntity(saved);
    }
    @CacheEvict(value="user", key="#id")
    public void delete(String id) {
        Optional<User> found = userRepository.findById(id);
        if (found.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        userRepository.delete(found.get());
    }
    @CachePut(value="Invoice", key="#id")
    public UserDto update(String id, UserInputDto inputDto) {

        Optional<User> found = userRepository.findById(id);
        if (found.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        Optional<String> invalid = inputDto.getRoles().stream().filter(role -> roleRepository.findByName(role)==null).findFirst();
        if (!invalid.isEmpty()) {
            throw new RoleNotFoundException(invalid.get());
        }

        User user = found.get();
        Set<Role> roles =  inputDto.getRoles().stream().map( name -> roleRepository.findByName(name)).collect(Collectors.toSet());

        user.setRoles(roles);
        user.setUsername(inputDto.getUsername());
        user.setFullName(inputDto.getFullName());
        user.setEmailAddress(inputDto.getEmailAddress());
        user.setStatus(inputDto.getStatus());

        userRepository.save(user);

        return UserDto.fromEntity(user);
    }
}
