package com.example.itDa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ItDaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItDaApplication.class, args);
	}

}
