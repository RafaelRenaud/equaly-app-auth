package com.br.equaly.auth.app.exception;

import java.util.function.Supplier;

public class CorporationNotFoundException extends RuntimeException {

    private static final String message = "Corporation or application keys not found, please contact the system administrator";

    public CorporationNotFoundException(){
        super(message);
    }
}
