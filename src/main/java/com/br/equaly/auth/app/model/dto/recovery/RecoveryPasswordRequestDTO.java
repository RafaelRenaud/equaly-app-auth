package com.br.equaly.auth.app.model.dto.recovery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RecoveryPasswordRequestDTO(

        @NotBlank
        @Size(max = 32)
        String login,

        @NotBlank
        String email,

        @NotBlank
        @Size(min = 6, max = 6)
        String code,

        @NotBlank
        String password
) {
}
