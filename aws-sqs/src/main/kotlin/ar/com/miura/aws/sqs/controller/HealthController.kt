package ar.com.miura.aws.sqs.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {
    @GetMapping("/health")
    fun getStatus():String {
        return "ok";
    }
}