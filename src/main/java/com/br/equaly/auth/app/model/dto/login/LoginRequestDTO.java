package com.br.equaly.auth.app.model.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(

        @NotBlank
        @Size(max = 32)
        String login,

        @NotBlank
        String password,

        @NotBlank
        @Size(max = 64)
        String domain
) {
}
