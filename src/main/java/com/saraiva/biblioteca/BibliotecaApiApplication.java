package com.saraiva.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApiApplication {

	public static void main(String[] args) {
		System.out.println(System.getenv("DB_PASSWORD") != null);
		SpringApplication.run(BibliotecaApiApplication.class, args);
	}

}
