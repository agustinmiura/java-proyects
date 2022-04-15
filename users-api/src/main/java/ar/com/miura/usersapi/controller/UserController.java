package ar.com.miura.usersapi.controller;

@lombok.extern.slf4j.Slf4j
@org.springframework.web.bind.annotation.RestController
public class UserController {
    private ar.com.miura.usersapi.service.UserService userService;
    @org.springframework.beans.factory.annotation.Autowired
    public UserController(ar.com.miura.usersapi.service.UserService userService) {
        this.userService = userService;
    }
    @org.springframework.web.bind.annotation.GetMapping("/users/{id}")
    public ar.com.miura.usersapi.dto.UserDto getUser(@org.springframework.web.bind.annotation.PathVariable("id") String id) {
        return userService.findOne(id);
    }

    @org.springframework.web.bind.annotation.GetMapping("/users")
    public java.util.List<ar.com.miura.usersapi.dto.UserDto> findAll() {
        return userService.findAll();
    }

    @org.springframework.web.bind.annotation.PostMapping("/users")
    public org.springframework.http.ResponseEntity<Object> createUser(@javax.validation.Valid @org.springframework.web.bind.annotation.RequestBody ar.com.miura.usersapi.dto.UserInputDto inputDto) {
        ar.com.miura.usersapi.dto.UserDto userDto = userService.save(inputDto);
        java.net.URI location = org.springframework.web.servlet.support.ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userDto.getId()).toUri();
        return org.springframework.http.ResponseEntity.created(location).build();
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/users/{id}")
    public void deleteUser(@org.springframework.web.bind.annotation.PathVariable("id") String id) {
        userService.delete(id);
    }


}
