package com.sparta.sprintbackofficeproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SprintBackofficeProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SprintBackofficeProjectApplication.class, args);
	}

}
