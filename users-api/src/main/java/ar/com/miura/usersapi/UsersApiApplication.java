package ar.com.miura.usersapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableCaching
@SpringBootApplication
public class UsersApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(UsersApiApplication.class, args);
	}

	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

}
