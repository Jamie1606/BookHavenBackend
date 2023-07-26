package com.bookshop.bookhaven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookhavenApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookhavenApplication.class, args);
	}

}
