package com.br.equaly.auth.app.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureServiceHubConfiguration {

    @Value("${spring.jms.email.queue-name}")
    private String queueName;

    @Bean
    public Queue queue(){
        return new Queue(this.queueName, Boolean.TRUE);
    }
}
