package com.kbtg.bootcamp.posttest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class PosttestApplication {

	public static void main(String[] args) {
		SpringApplication.run(PosttestApplication.class, args);
	}

}
