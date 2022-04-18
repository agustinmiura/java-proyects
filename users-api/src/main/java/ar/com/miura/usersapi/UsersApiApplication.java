package ar.com.miura.usersapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class UsersApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(UsersApiApplication.class, args);
	}

}
