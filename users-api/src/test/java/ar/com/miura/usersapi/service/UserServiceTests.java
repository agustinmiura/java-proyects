package ar.com.miura.usersapi.service;

import ar.com.miura.usersapi.dto.UserDto;
import ar.com.miura.usersapi.dto.UserInputDto;
import ar.com.miura.usersapi.entity.Role;
import ar.com.miura.usersapi.entity.User;
import ar.com.miura.usersapi.repository.RoleRepository;
import ar.com.miura.usersapi.repository.UserRepository;
import ar.com.miura.usersapi.utils.IdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UserServiceTests {

    private IdGenerator idGenerator = new IdGenerator();
    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private RoleRepository roleRepository = Mockito.mock(RoleRepository.class);

    private UserService userService = new UserService(idGenerator, userRepository, roleRepository);

    @Test
    public void shouldFindAll() {

        User user = new User(
                "id",
                "username",
                "fullName",
                new HashSet(),
                "address@gmail.com",
                "status",
                LocalDateTime.now(),
                null
        );
        List<User> users = List.of(user);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());

        Page<User> page = new PageImpl<>(users, pageRequest, users.size());
        Mockito.when(userRepository.findAll(pageRequest)).thenReturn(page);

        var foundList = userService.findAll(pageRequest);
        Mockito.verify(userRepository, Mockito.times(1)).findAll(pageRequest);
        Assertions.assertEquals(1, users.size());
        var found = foundList.get(0);
        Assertions.assertEquals("id", found.getId());
        Assertions.assertEquals("username", found.getUsername());
        Assertions.assertEquals("fullName", found.getFullName());
        Assertions.assertEquals("address@gmail.com", found.getEmailAddress());
        Assertions.assertEquals("status", found.getStatus());

    }

    @Test
    public void shouldFindOne() {
        Mockito.when(userRepository.findById("id")).thenReturn(Optional.of(new User(
                "id",
                "username",
                "fullName",
                new HashSet(),
                "address@gmail.com",
                "status",
                LocalDateTime.now(),
                null
        )));
        var found = userService.findOne("id");
        Mockito.verify(userRepository, Mockito.times(1)).findById("id");
        Assertions.assertEquals("id", found.getId());
    }

    @Test
    public void shouldSaveUser() {

        Role role = new Role("1", "role_1", LocalDateTime.now(), null);

        User user = new User(
                "id",
                "username",
                "fullName",
                Set.of(role),
                "address@gmail.com",
                "status",
                LocalDateTime.now(),
                null
        );

        UserDto dto = new UserDto(
                "id",
                "username",
                "fullName",
                Set.of("role_1"),
                "address@gmail.com",
                "status"
        );
        UserInputDto userInputDto = new UserInputDto(
            "username",
            "fullName",
            List.of("role_1"),
            "address@gmail.com",
            "status"
        );

        Mockito.when(roleRepository.findByName("role_1")).thenReturn(role);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        var created = userService.save(userInputDto);
        Assertions.assertEquals(created, dto);

    }

    @Test
    public void shouldDeleteUser() {
        Role role = new Role("1", "role_1", LocalDateTime.now(), null);
        User user = new User(
                "id",
                "username",
                "fullName",
                Set.of(role),
                "address@gmail.com",
                "status",
                LocalDateTime.now(),
                null
        );

        Mockito.when(userRepository.findById("id")).thenReturn(Optional.of(user));
        userService.delete("id");
        Mockito.verify(userRepository).findById("id");
        Mockito.verify(userRepository).delete(user);

    }

    @Test
    public void shouldUpdateEntity() {

        Role role = new Role("1", "role_1", LocalDateTime.now(), null);
        User user = new User(
                "id",
                "username",
                "fullName",
                Set.of(role),
                "address@gmail.com",
                "status",
                LocalDateTime.now(),
                null
        );

        Mockito.when(userRepository.findById("id")).thenReturn(Optional.of(user));
        Mockito.when(roleRepository.findByName("role_1")).thenReturn(role);

        UserInputDto userInputDto = new UserInputDto(
    "newUsername",
    "newFullname",
            List.of("role_1"),
"email@gmail.com",
     "newStatus"
        );

        var updated = userService.update("id", userInputDto);

        Assertions.assertEquals("id", updated.getId());

        Assertions.assertEquals("newUsername", updated.getUsername());
        Assertions.assertEquals("newFullname", updated.getFullName());
        Assertions.assertEquals("id", updated.getId());
        Assertions.assertEquals("email@gmail.com", updated.getEmailAddress());
        Assertions.assertEquals("newStatus", updated.getStatus());
    }


}
