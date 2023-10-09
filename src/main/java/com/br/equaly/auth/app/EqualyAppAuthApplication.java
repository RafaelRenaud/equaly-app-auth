package com.br.equaly.auth.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class EqualyAppAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(EqualyAppAuthApplication.class, args);
	}

}
