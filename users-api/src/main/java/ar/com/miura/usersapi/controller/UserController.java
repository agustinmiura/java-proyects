package ar.com.miura.usersapi.controller;

import ar.com.miura.usersapi.dto.UserDto;
import ar.com.miura.usersapi.dto.UserInputDto;
import ar.com.miura.usersapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/v1/users/{id}")
    public UserDto getUser(@PathVariable("id") String id) {
        return userService.findOne(id);
    }

    @GetMapping("/v1/users")
    public java.util.List<UserDto> findAll() {
        return userService.findAll();
    }

    @PostMapping("/v1/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserInputDto inputDto) {
        try {
            UserDto userDto = userService.save(inputDto);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(userDto.getId()).toUri();
            return ResponseEntity.created(location).build();
        }catch(Exception e) {
            throw e;
        }
    }

    @DeleteMapping("/v1/users/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        userService.delete(id);
    }


}
