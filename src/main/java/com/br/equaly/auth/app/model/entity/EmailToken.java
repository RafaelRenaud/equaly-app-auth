package com.br.equaly.auth.app.model.entity;

import com.br.equaly.auth.app.model.enums.EmailTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailToken {

    private String id;

    private EmailTemplate templateType;

    private String timestamp;
}
