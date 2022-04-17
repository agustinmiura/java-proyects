package ar.com.miura.usersapi;

import ar.com.miura.usersapi.validator.EmailValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationTests {

    private EmailValidator validator = new EmailValidator();

    @Test
    public void shouldValidateEmails() {
        assertTrue(validator.isValid("hola@gmail.com"));
    }

    @Test
    public void shouldFindInvalidEmails() {
        assertFalse(validator.isValid("holadfdsdf"));
        assertFalse(validator.isValid(""));

    }
}
