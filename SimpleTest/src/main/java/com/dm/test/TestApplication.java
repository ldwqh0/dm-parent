package com.dm.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan
@ComponentScan
@EnableJpaRepositories
public class TestApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TestApplication.class, args);
	}
}
