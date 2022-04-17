package ar.com.miura.usersapi.repositories;

import ar.com.miura.usersapi.entity.Role;
import ar.com.miura.usersapi.repository.RoleRepository;
import ar.com.miura.usersapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RoleRepositoyTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void shouldFindRepository() {

        Role role = new Role("id", "name", LocalDateTime.now(), null);
        roleRepository.save(role);

        Role roleFound = roleRepository.findById(role.getId()).get();
        Assertions.assertEquals(role.getId(), roleFound.getId());

    }


}
