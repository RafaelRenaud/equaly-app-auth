package com.br.equaly.auth.app.service;

import com.br.equaly.auth.app.model.dto.recovery.RecoveryEmailRequestDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

public interface RecoveryEmailService {

    void sendRecoveryEmail(@RequestBody RecoveryEmailRequestDTO recoveryEmailRequestDTO) throws IOException;

}
