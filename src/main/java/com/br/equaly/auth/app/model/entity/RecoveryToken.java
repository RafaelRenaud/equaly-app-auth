package com.br.equaly.auth.app.model.entity;

import com.br.equaly.auth.app.model.enums.EmailTemplate;
import com.br.equaly.auth.app.util.UtilTools;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "RecoveryToken", timeToLive = 1800)
public class RecoveryToken extends EmailToken{

    private String name;

    private String login;

    private String email;

    private String code;

    public RecoveryToken(String id, EmailTemplate templateType, String timestamp, String name, String login, String email){
        super(id, templateType, timestamp);
        this.name = name;
        this.login = login;
        this.email = email;
        this.code = UtilTools.generateRecoveryCode();
    }

}
