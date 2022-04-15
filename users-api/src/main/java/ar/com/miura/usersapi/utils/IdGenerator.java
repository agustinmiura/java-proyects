package ar.com.miura.usersapi.utils;

@org.springframework.stereotype.Component
public class IdGenerator {

    public String generateId() {
        return java.util.UUID.randomUUID().toString();
    }

}
