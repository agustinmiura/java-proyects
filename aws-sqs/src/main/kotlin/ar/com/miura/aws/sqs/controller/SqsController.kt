package ar.com.miura.aws.sqs.controller

import ar.com.miura.aws.sqs.dto.InputMessageDto
import ar.com.miura.aws.sqs.dto.MessageDto
import ar.com.miura.aws.sqs.service.SqsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SqsController(@Autowired val sqsService: SqsService) {
    @PostMapping("/sqs/messages")
    fun createMessages(@RequestBody message: InputMessageDto):MessageDto {
        return sqsService.sendMessage(message);
    }
    @GetMapping("/sqs/messages")
    fun getMessages():List<MessageDto> {
        val messages = sqsService.receiveMessages();
        return messages;
    }
}