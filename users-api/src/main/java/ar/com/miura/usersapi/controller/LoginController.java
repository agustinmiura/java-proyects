package ar.com.miura.usersapi.controller;

import ar.com.miura.usersapi.dto.LoginInputDto;
import ar.com.miura.usersapi.dto.LoginResponseDto;
import ar.com.miura.usersapi.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/v1/authentication")
    public LoginResponseDto login(@RequestBody LoginInputDto loginInputDto) {
        return loginService.login(loginInputDto);
    }

}
