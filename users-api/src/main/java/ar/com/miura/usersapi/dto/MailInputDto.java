package ar.com.miura.usersapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class MailInputDto implements Serializable  {
    @Email
    private String email;
}
