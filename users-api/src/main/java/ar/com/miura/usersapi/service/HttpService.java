package ar.com.miura.usersapi.service;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
public class HttpService {
    @Retry(name = "http-retry")
    public <T> ResponseEntity<T> getRequest(WebClient webClient, Map<String,String> headers, Class clazz) {

        log.info(" Http service execution on : {} ", LocalDateTime.now());

        RequestHeadersUriSpec request = webClient.get();
        request.accept(MediaType.APPLICATION_JSON);
        for(Map.Entry<String,String> entry:headers.entrySet()) {
            request.header(entry.getKey(), entry.getValue());
        }
        return (ResponseEntity<T>) request.retrieve()
                .toEntity(clazz)
                .block();
    }


}
