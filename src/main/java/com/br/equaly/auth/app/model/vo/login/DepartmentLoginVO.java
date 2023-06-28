package com.br.equaly.auth.app.model.vo.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentLoginVO {
    private Boolean isActive;
    private String name;
}
