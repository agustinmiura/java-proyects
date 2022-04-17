package ar.com.miura.usersapi.integration;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerIT.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetOneUser() throws IOException {
        String response = this.restTemplate.getForObject("/v1/users/id1", String.class);
        String expected = CustomFileUtils.readContent("json/get_one_ok.json");
        Assertions.assertEquals(expected, response);
    }

    @Test
    public void shouldCreateAnUser() throws IOException {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(CustomFileUtils.readContent("json/create_user_request_ok.json"), headers);
        UserDto result = restTemplate.postForObject("/v1/users", request, UserDto.class);
        Assertions.assertNotNull(result.getId());
    }

    @Test
    public void shouldDeleteAnUser() throws IOException {
        var headers = new HttpHeaders();
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
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(CustomFileUtils.readContent("json/update_user_ok_request.json"), headers);
        ResponseEntity<String> response = restTemplate
                .exchange("/v1/users/id1", HttpMethod.PUT, request, String.class);
        String body = response.getBody();
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals(CustomFileUtils.readContent("json/update_user_ok_response.json"), body);
    }



}
