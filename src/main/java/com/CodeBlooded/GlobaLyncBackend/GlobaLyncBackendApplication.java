package com.CodeBlooded.GlobaLyncBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(
		basePackages = "com.CodeBlooded.GlobaLyncBackend.repositories.jpa"
)
public class GlobaLyncBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(GlobaLyncBackendApplication.class, args);
	}
}
