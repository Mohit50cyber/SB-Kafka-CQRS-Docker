package com.ProductQuery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class ProductQueryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductQueryApplication.class, args);
	}

}
