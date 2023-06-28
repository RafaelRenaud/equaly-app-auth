package com.br.equaly.auth.app.model.dto.refresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RefreshSessionRequestDTO(
        @NotBlank
        @JsonProperty("refresh_token")
        String refreshToken
) {
}
