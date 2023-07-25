package com.br.equaly.auth.app.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessengerConfiguration {

    @Value("${spring.rabbitmq.messenger.queue}")
    private String messengerQueue;

    @Bean
    public Queue queue(){
        return new Queue(messengerQueue, true);
    }
}
