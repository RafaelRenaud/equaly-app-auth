package com.br.equaly.auth.app.model.dto.recovery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RecoveryRequestDTO(

        @NotBlank
        @Size(max = 32)
        String login,

        @NotBlank
        String email,

        @NotBlank
        @Size(max = 64)
        String domain
) {
}
