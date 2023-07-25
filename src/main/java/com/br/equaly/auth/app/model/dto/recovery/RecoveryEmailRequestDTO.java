package com.br.equaly.auth.app.model.dto.recovery;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
public class RecoveryEmailRequestDTO implements Serializable {
    @NotBlank
    String token;

    @NotBlank
    String messageType;

    @NotBlank
    String email;
}
