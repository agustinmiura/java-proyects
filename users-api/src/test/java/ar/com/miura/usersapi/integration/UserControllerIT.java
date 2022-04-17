package ar.com.miura.usersapi.integration;

import ar.com.miura.usersapi.utils.CustomFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.util.ClassPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerIT.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetUsers() throws IOException {
        String response = this.restTemplate.getForObject("/v1/users", String.class);
        String expected = CustomFileUtils.readContent("json/get_all_ok.json");
        Assertions.assertEquals(expected, response);
    }

    @Test
    public void shouldGetOneUser() throws IOException {
        String response = this.restTemplate.getForObject("/v1/users/id1", String.class);
        String expected = CustomFileUtils.readContent("json/get_one_ok.json");
        Assertions.assertEquals(expected, response);
    }


}
