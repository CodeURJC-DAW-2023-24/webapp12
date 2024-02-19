package es.codeurjc.aplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class HellowordVscodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(HellowordVscodeApplication.class, args);
	}

}
