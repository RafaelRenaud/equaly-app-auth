package com.br.equaly.auth.app.service;

import com.br.equaly.auth.app.model.dto.recovery.RecoveryPasswordRequestDTO;
import com.br.equaly.auth.app.model.entity.RecoveryToken;
import com.br.equaly.auth.app.model.entity.User;

public interface RecoveryService {
    User verifyAccount(String login, String email, String domain);
    Boolean isValidAccount(RecoveryToken recoveryToken, RecoveryPasswordRequestDTO recoveryRequestDTO);
    void changePassword(RecoveryPasswordRequestDTO recoveryRequestDTO);
}
