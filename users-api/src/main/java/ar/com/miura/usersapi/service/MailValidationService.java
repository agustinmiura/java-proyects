package ar.com.miura.usersapi.service;

import ar.com.miura.usersapi.dto.EmailApiResponse;
import ar.com.miura.usersapi.dto.MailInputDto;
import ar.com.miura.usersapi.dto.MailValidationStatus;
import ar.com.miura.usersapi.misc.EnvReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Component
public class MailValidationService {

    public static final String VALID_STATUS = "valid";
    public static final String RAPIDAPI_EMAIL_API = "rapidapi.email.api";
    public static final String V_2_VALIDATE_EMAIL_$_EMAIL_IP_ADDRESS = "/v2/validate?email=$email&ip_address";
    public static final String EMAIL = "$email";
    public static final String X_RAPID_API_HOST_KEY = "X-RapidAPI-Host";
    public static final String RAPIDAPI_HOSTS = "rapidapi.hosts";
    public static final String RAPIDAPI_KEY = "rapidapi.key";
    public static final String X_RAPID_API_KEY_MAP_KEY = "X-RapidAPI-Key";
    private EnvReader envReader;

    private HttpService httpService;

    @Autowired
    public MailValidationService(EnvReader envReader, HttpService httpService) {
        this.envReader = envReader;
        this.httpService = httpService;
    }

    public MailValidationStatus getStatus(@Valid MailInputDto mailInputDto) {

        String url = envReader.getProperty(RAPIDAPI_EMAIL_API) + V_2_VALIDATE_EMAIL_$_EMAIL_IP_ADDRESS;
        url = url.replace(EMAIL, mailInputDto.getEmail());

        log.info(" I see the address ");

        Map<String,String> headers = Map.of(
            X_RAPID_API_HOST_KEY,
            envReader.getProperty(RAPIDAPI_HOSTS),
            X_RAPID_API_KEY_MAP_KEY,
            envReader.getProperty(RAPIDAPI_KEY)
        );
        ResponseEntity<EmailApiResponse> response = httpService.getRequest(
            WebClient.create(url),
            headers,
            EmailApiResponse.class
        );

        log.info(" I see the body : " + response.getBody());

        if (response.getStatusCodeValue() == HttpStatus.OK.value()
            && response.getBody()!=null
            && response.getBody().getStatus().equals(VALID_STATUS)) {
            return new MailValidationStatus(true, mailInputDto.getEmail(), "");
        } else if (response.getStatusCodeValue() == HttpStatus.OK.value()
            && response.getBody()!=null
            && !response.getBody().getStatus().equals(VALID_STATUS)) {
            return new MailValidationStatus(false, mailInputDto.getEmail(), response.getBody().getSub_status());
        } else {
            return new MailValidationStatus(false, mailInputDto.getEmail(), response.getStatusCode().toString());
        }

    }
}
