package ar.com.miura.usersapi.service;

import ar.com.miura.usersapi.dto.LoginInputDto;
import ar.com.miura.usersapi.dto.LoginResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginService {

    private JwtService jwtService;

    @Autowired
    public LoginService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    public LoginResponseDto login(LoginInputDto loginInputDto) {
        String user = loginInputDto.getUser();
        String password = loginInputDto.getPassword();

        if (user.equals("user") && password.equals("password")) {
            return new LoginResponseDto(true, jwtService.getToken("subject"));
        } else {
            return new LoginResponseDto(false, "");
        }

    }
}
