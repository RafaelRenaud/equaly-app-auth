package com.br.equaly.auth.app.service.impl;

import com.br.equaly.auth.app.model.dto.recovery.RecoveryEmailRequestDTO;
import com.br.equaly.auth.app.service.RecoveryEmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RecoveryEmailServiceImpl implements RecoveryEmailService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${spring.jms.email.queue-name}")
    private String queueName;

    @Override
    public void sendRecoveryEmail(RecoveryEmailRequestDTO recoveryEmailRequestDTO) throws IOException {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        jmsTemplate.convertAndSend(
                queueName,
                objectWriter.writeValueAsBytes(recoveryEmailRequestDTO)
        );
    }
}
