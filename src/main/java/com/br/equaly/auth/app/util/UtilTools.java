package com.br.equaly.auth.app.util;

import java.util.Random;

public class UtilTools {
    public static final Integer CODE_SIZE = 6;

    public static String generateRecoveryCode(){
        StringBuilder code = new StringBuilder(String.valueOf(new Random().nextLong(1L, 999999L)));

        while(code.length() < CODE_SIZE){
            code.insert(0, "0");
        }

        return code.toString();
    }
}
