package com.br.equaly.auth.app;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class EqualyAppAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(EqualyAppAuthApplication.class, args);
	}

}
