package ar.com.miura.usersapi.validator;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidator {
    private String regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public boolean isValid(String address) {
        return Pattern.compile(regexPattern)
                .matcher(address)
                .matches();
    }
}
