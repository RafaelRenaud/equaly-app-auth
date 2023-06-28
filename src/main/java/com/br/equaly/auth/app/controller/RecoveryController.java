package com.br.equaly.auth.app.controller;

import com.br.equaly.auth.app.model.dto.recovery.RecoveryPasswordRequestDTO;
import com.br.equaly.auth.app.model.dto.recovery.RecoveryRequestDTO;
import com.br.equaly.auth.app.model.entity.RecoveryToken;
import com.br.equaly.auth.app.model.entity.User;
import com.br.equaly.auth.app.service.impl.RecoveryServiceImpl;
import com.br.equaly.auth.app.service.impl.TokenServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recovery")
public class RecoveryController {

    @Autowired
    private RecoveryServiceImpl recoveryService;

    @Autowired
    private TokenServiceImpl tokenService;

    @PostMapping
    public ResponseEntity sendRecoveryMessage(
            @RequestBody
            @Valid
                    RecoveryRequestDTO recoveryRequestDTO
    ){

        User validatedUser = recoveryService.verifyAccount(
                recoveryRequestDTO.login(), recoveryRequestDTO.email(), recoveryRequestDTO.domain());

        if(validatedUser != null){
            String recoveryTokenId = tokenService.generateRecoveryToken(validatedUser);
            //Aqui deve chamar a API de Email
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{recovery_id}")
    public ResponseEntity changePassword(
            @PathVariable("recovery_id") String recoveryTokenId,
            @RequestBody
            @Valid
                    RecoveryPasswordRequestDTO recoveryRequestDTO
    ){
        RecoveryToken recoveryToken = tokenService.getRecoveryToken(recoveryTokenId);
        Boolean isValidAccount = recoveryService.isValidAccount(recoveryToken, recoveryRequestDTO);

        if(isValidAccount){
            recoveryService.changePassword(recoveryRequestDTO);
            tokenService.removeRecoverySession(recoveryToken.getId());
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
