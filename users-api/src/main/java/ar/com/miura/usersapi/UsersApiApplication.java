package ar.com.miura.usersapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsersApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(UsersApiApplication.class, args);
	}

	/*
	@org.springframework.context.annotation.Bean(name = "dataSource")
	public org.springframework.jdbc.datasource.DriverManagerDataSource dataSource() {
		org.springframework.jdbc.datasource.DriverManagerDataSource dataSource = new org.springframework.jdbc.datasource.DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:~/myDB;MV_STORE=false");
		dataSource.setUsername("sa");
		dataSource.setPassword("");

		// schema init
		org.springframework.core.io.Resource initSchema = new org.springframework.core.io.ClassPathResource("scripts/schema-h2.sql");
		org.springframework.core.io.Resource initData = new org.springframework.core.io.ClassPathResource("scripts/data-h2.sql");
		org.springframework.jdbc.datasource.init.DatabasePopulator databasePopulator = new org.springframework.jdbc.datasource.init.ResourceDatabasePopulator(initSchema, initData);
		org.springframework.jdbc.datasource.init.DatabasePopulatorUtils.execute(databasePopulator, dataSource);

		return dataSource;
	}
	*/

}
