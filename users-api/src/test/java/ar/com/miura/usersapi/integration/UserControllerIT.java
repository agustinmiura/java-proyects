package ar.com.miura.usersapi.integration;

import ar.com.miura.usersapi.dto.LoginInputDto;
import ar.com.miura.usersapi.dto.LoginResponseDto;
import ar.com.miura.usersapi.dto.UserDto;
import ar.com.miura.usersapi.utils.CustomFileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class UserControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerIT.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @WithMockUser("user")
    public void shouldGetOneUser() throws IOException {
        LoginResponseDto loginResponseDto = login();
        HttpHeaders headers = getHttpHeaders(loginResponseDto);

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange("/v1/users/id1", HttpMethod.GET, entity, String.class);

        String expected = CustomFileUtils.readContent("json/get_one_ok.json");
        Assertions.assertEquals(expected, response.getBody());

    }



    @Test
    public void shouldCreateAnUser() throws IOException {
        LoginResponseDto loginResponseDto = login();
        HttpHeaders headers = getHttpHeaders(loginResponseDto);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<String>(CustomFileUtils.readContent("json/create_user_request_ok.json"), headers);
        UserDto result = restTemplate.postForObject("/v1/users", request, UserDto.class);
        Assertions.assertNotNull(result.getId());
    }

    @Test
    public void shouldDeleteAnUser() throws IOException {
        LoginResponseDto loginResponseDto = login();

        HttpHeaders headers = getHttpHeaders(loginResponseDto);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(new String(""), headers);
        ResponseEntity<String> response = restTemplate
                .exchange("/v1/users/id1", HttpMethod.DELETE, request, String.class);
        String body = response.getBody();
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals(null, body);
    }

    @Test
    public void shouldUpdateAnUser() throws IOException {
        LoginResponseDto loginResponseDto = login();

        HttpHeaders headers = getHttpHeaders(loginResponseDto);
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(CustomFileUtils.readContent("json/update_user_ok_request.json"), headers);
        ResponseEntity<String> response = restTemplate
                .exchange("/v1/users/id1", HttpMethod.PUT, request, String.class);
        String body = response.getBody();
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals(CustomFileUtils.readContent("json/update_user_ok_response.json"), body);
    }

    private LoginResponseDto login() {
        LoginInputDto inputDto = new LoginInputDto("user", "password");
        LoginResponseDto loginResponseDto = this.restTemplate.postForObject("/v1/authentication", inputDto, LoginResponseDto.class);
        return loginResponseDto;
    }

    private HttpHeaders getHttpHeaders(LoginResponseDto loginResponseDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, loginResponseDto.getJwtToken());
        return headers;
    }


}
