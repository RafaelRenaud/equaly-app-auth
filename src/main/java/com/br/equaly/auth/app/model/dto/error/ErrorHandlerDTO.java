package com.br.equaly.auth.app.model.dto.error;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ErrorHandlerDTO {

    private String errorCode;
    private String message;
    private String details;
    private String traceId;
    private String timestamp;

    public ErrorHandlerDTO(HttpStatus httpStatus, Exception e){
        this.setErrorCode(String.valueOf(httpStatus.value()));
        this.setMessage(e.getMessage());
        this.setDetails(e.getLocalizedMessage());
        this.setTraceId(UUID.randomUUID().toString());
        this.setTimestamp(LocalDateTime.now().toString());
    }
}
