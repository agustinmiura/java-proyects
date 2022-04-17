package ar.com.miura.usersapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
public class MailInputDto {
    @Email
    private String email;
}
