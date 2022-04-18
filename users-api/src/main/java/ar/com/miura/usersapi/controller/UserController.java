package ar.com.miura.usersapi.controller;

import ar.com.miura.usersapi.SortingEnum;
import ar.com.miura.usersapi.dto.UserDto;
import ar.com.miura.usersapi.dto.UserInputDto;
import ar.com.miura.usersapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public java.util.List<UserDto> findAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "desc") String order) {
        try {
            PageRequest pageRequest = PageRequest.of(
                    page,
                    pageSize,
                    order.toLowerCase().equals(SortingEnum.DESC.getValue()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending() )
                    ;
            return userService.findAll(pageRequest);
        }catch(Exception e) {
            throw e;
        }
    }

    @PostMapping("/v1/users")
    public UserDto createUser(@Valid @RequestBody UserInputDto inputDto) {
        try {
            UserDto userDto = userService.save(inputDto);
            return userDto;
        }catch(Exception e) {
            throw e;
        }
    }

    @DeleteMapping("/v1/users/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        try {
            userService.delete(id);
        }catch(Exception e) {
            throw e;
        }
    }

    @PutMapping("v1/users/{id}")
    public UserDto updateUser(@PathVariable("id") String id, @RequestBody UserInputDto inputDto) {
        try {
            return userService.update(id, inputDto);
        }catch(Exception e) {
            throw e;
        }
    }


}
