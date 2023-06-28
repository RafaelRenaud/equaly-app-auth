package com.br.equaly.auth.app.model.entity;

import com.br.equaly.auth.app.model.enums.EmailTemplate;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RedisHash(value = "RecoveryToken", timeToLive = 1800)
public class RecoveryToken {

    @Id
    private String id;

    private String templateType;

    private String name;

    private String login;

    private String email;

    private String timestamp;

}
