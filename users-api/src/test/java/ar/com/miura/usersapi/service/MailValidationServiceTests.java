package ar.com.miura.usersapi.service;

import ar.com.miura.usersapi.dto.EmailApiResponse;
import ar.com.miura.usersapi.dto.MailInputDto;
import ar.com.miura.usersapi.dto.MailValidationStatus;
import ar.com.miura.usersapi.misc.EnvReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import static ar.com.miura.usersapi.service.MailValidationService.RAPIDAPI_EMAIL_API;
import static ar.com.miura.usersapi.service.MailValidationService.RAPIDAPI_HOSTS;
import static ar.com.miura.usersapi.service.MailValidationService.RAPIDAPI_KEY;
import static ar.com.miura.usersapi.service.MailValidationService.VALID_STATUS;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MailValidationServiceTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailValidationServiceTests.class);

    private EnvReader envReader = Mockito.mock(EnvReader.class);

    private HttpService httpService = Mockito.mock(HttpService.class);

    private WebClient webClient = Mockito.mock(WebClient.class);

    private ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);

    private MailValidationService mailValidationService = new MailValidationService(envReader, httpService);

    @Test
    public void shouldGetStatus() {

        Mockito.when(
            envReader.getProperty(RAPIDAPI_EMAIL_API)
        ).thenReturn(
        "http://localhost:8080/mail-validation-service/api/v1/validate"
        );

        Mockito.when(envReader.getProperty(RAPIDAPI_HOSTS)).thenReturn("host");
        Mockito.when(envReader.getProperty(RAPIDAPI_KEY)).thenReturn("key");

        Mockito.when(httpService.getRequest(
            any(),
            any(),
            any()
        )).thenReturn(responseEntity);

        var emailResponse = new EmailApiResponse();
        emailResponse.setStatus(VALID_STATUS);
        Mockito.when(responseEntity.getStatusCodeValue()).thenReturn(HttpStatus.OK.value());
        Mockito.when(responseEntity.getBody()).thenReturn(emailResponse);

        MailValidationStatus mailValidationStatus = mailValidationService.getStatus(
            new MailInputDto("email@gmail.com")
        );

        Assertions.assertTrue(mailValidationStatus.isValid());
        Assertions.assertEquals("", mailValidationStatus.getReason());
        Assertions.assertEquals("email@gmail.com", mailValidationStatus.getEmail());
    }

}
