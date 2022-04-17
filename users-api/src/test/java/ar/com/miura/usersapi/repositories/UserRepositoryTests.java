package ar.com.miura.usersapi.repositories;

import ar.com.miura.usersapi.entity.User;
import ar.com.miura.usersapi.repository.RoleRepository;
import ar.com.miura.usersapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldFindAll() {
        Iterable<User> iterable = userRepository.findAll();
        List<User> users = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
        Assertions.assertEquals(4, users.size());
    }

}
