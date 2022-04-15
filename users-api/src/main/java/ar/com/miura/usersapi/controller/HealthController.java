package ar.com.miura.usersapi.controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@org.springframework.web.bind.annotation.RestController
public class HealthController {
    @org.springframework.web.bind.annotation.GetMapping("/health")
    public String getStatus() {
        log.info("/health");
        return "ok";
    }
}
