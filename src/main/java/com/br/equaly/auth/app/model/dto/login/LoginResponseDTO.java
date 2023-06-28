package com.br.equaly.auth.app.model.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseDTO(
        String timestamp,

        @JsonProperty(value = "trace_id")
        String traceId,

        @JsonProperty(value = "access_token")
        String accessToken,

        @JsonProperty(value = "refresh_token")
        String refreshToken
) {
}
