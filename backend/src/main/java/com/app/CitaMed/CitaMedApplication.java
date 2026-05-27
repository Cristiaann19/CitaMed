package com.app.CitaMed;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync

public class CitaMedApplication {
	public static void main(String[] args) {
		SpringApplication.run(CitaMedApplication.class, args);
	}
}
