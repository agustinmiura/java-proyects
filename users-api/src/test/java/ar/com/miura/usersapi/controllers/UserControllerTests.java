package ar.com.miura.usersapi.controllers;

import ar.com.miura.usersapi.SortingEnum;
import ar.com.miura.usersapi.controller.UserController;
import ar.com.miura.usersapi.dto.UserDto;
import ar.com.miura.usersapi.dto.UserInputDto;
import ar.com.miura.usersapi.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserControllerTests {

    private UserService userService = Mockito.mock(UserService.class);

    private UserController userController = new UserController(userService);

    @Test
    public void shouldFindAll() {

        PageRequest pageRequest = PageRequest.of(
                0,
                10,
                Sort.by("id").descending());

        UserDto dto = new UserDto(
            "id",
            "username",
            "fullName",
            new HashSet(),
            "address@gmail.com",
            "status"
        );
        var dtos = new ArrayList();
        dtos.add(dto);

        Mockito.when(userService.findAll(pageRequest)).thenReturn(dtos);
        var foundDtos = userController.findAll(0, 10, "id", "desc");

        Assertions.assertEquals(1, foundDtos.size());
        var found = foundDtos.get(0);
        Assertions.assertEquals("id", found.getId());
        Assertions.assertEquals("username", found.getUsername());
        Assertions.assertEquals("fullName", found.getFullName());
        Assertions.assertEquals("address@gmail.com", found.getEmailAddress());
        Assertions.assertEquals("status", found.getStatus());

    }

    @Test
    public void shouldCreateUser() {

        var dto = new UserInputDto(
            "username",
            "fullName",
            List.of("role_1", "role_2", "role_3"),
"user@gmail.com",
     "status"
        );

        UserDto userDto = new UserDto(
            "id",
            "username",
            "fullName",
            Set.of("role_1", "role_2", "role_3"),
            "user@gmail.com",
            "status"
        );
        Mockito.when(userService.save(dto)).thenReturn(userDto);
        var outputResponse = userController.createUser(dto);
        Assertions.assertEquals(userDto, outputResponse);

    }

    @Test
    public void shouldDeleteUser() {
        userController.deleteUser("id");
        Mockito.verify(userService).delete("id");
    }

    @Test
    public void shouldUpdateAnUser() {

        var dto = new UserInputDto(
                "username",
                "fullName",
                List.of("role_1", "role_2", "role_3"),
                "user@gmail.com",
                "status"
        );

        UserDto userDto = new UserDto(
                "id",
                "username",
                "fullName",
                Set.of("role_1", "role_2", "role_3"),
                "user@gmail.com",
                "status"
        );
        Mockito.when(userService.update("id",dto)).thenReturn(userDto);

        var updated = userController.updateUser("id", dto);
        Assertions.assertEquals(userDto, updated);
        Mockito.verify(userService).update("id", dto);

    }





}
