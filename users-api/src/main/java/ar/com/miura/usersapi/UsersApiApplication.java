package ar.com.miura.usersapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity(debug = true)
@EnableCaching
@SpringBootApplication
public class UsersApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(UsersApiApplication.class, args);
	}

}
