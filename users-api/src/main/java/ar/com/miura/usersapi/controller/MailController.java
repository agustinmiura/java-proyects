package ar.com.miura.usersapi.controller;

import ar.com.miura.usersapi.dto.MailInputDto;
import ar.com.miura.usersapi.dto.MailValidationStatus;
import ar.com.miura.usersapi.exception.InvalidEmailAddress;
import ar.com.miura.usersapi.service.MailValidationService;
import ar.com.miura.usersapi.validator.EmailValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MailController {

    private MailValidationService service;

    private EmailValidator emailValidator;

    @Autowired
    public MailController(EmailValidator emailValidator,MailValidationService service) {
        this.service = service;
        this.emailValidator = emailValidator;
    }

    @GetMapping("/v1/mail/status")
    public MailValidationStatus getStatus(@RequestParam String mail) {
        try {
            if (!emailValidator.isValid(mail)) {
                throw new InvalidEmailAddress(mail);
            }
            return service.getStatus(new MailInputDto(mail));
        }catch(Exception e) {
            throw e;
        }
    }

}
