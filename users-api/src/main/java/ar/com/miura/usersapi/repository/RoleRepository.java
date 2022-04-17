package ar.com.miura.usersapi.repository;

import ar.com.miura.usersapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByName(String name);

}
