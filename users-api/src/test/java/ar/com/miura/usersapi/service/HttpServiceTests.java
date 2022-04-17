package ar.com.miura.usersapi.service;

import ar.com.miura.usersapi.dto.EmailApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static ar.com.miura.usersapi.service.MailValidationService.VALID_STATUS;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HttpServiceTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServiceTests.class);

    @Mock
    private WebClient webClientMock;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;

    @Mock
    private WebClient.ResponseSpec responseMock;

    private HttpService httpService = new HttpService();

    @BeforeEach
    void setUp() {
    }

    @Test
    public void shouldMakeOkRequest() {

        EmailApiResponse response = new EmailApiResponse();
        response.setStatus(VALID_STATUS);

        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.retrieve()).thenReturn(responseMock);
        when(responseMock.toEntity(EmailApiResponse.class)).thenReturn(Mono.just(ResponseEntity.ok(response)));

        Map<String,String> headers = new HashMap<>();

        ResponseEntity<EmailApiResponse> responseEntity = httpService.getRequest(
            webClientMock,
            headers,
            EmailApiResponse.class
        );
        Assertions.assertEquals(VALID_STATUS, responseEntity.getBody().getStatus());

    }

}
