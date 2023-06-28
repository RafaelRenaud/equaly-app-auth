package com.br.equaly.auth.app.model.vo.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorporationLoginVO {
    private String name;
    private String alias;
    private Boolean isActive;
    private String appkey;

    public CorporationLoginVO(String name, String alias, Boolean isActive){
        this.name = name;
        this.alias = alias;
        this.isActive = isActive;
    }
}
