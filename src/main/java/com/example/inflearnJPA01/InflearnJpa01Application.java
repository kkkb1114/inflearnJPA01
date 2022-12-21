package com.example.inflearnJPA01;

import com.example.inflearnJPA01.domain.Hello;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InflearnJpa01Application {

	public static void main(String[] args) {
		SpringApplication.run(InflearnJpa01Application.class, args);
	}

	@Bean
	Hibernate5Module hibernate5Module(){
		Hibernate5Module hibernate5Module = new Hibernate5Module();
		return new Hibernate5Module();
	}

}
