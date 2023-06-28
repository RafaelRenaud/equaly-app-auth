package com.br.equaly.auth.app.exception;

public class UserValidationException extends RuntimeException{
    private static final String message = "Invalid User or Token";

    public UserValidationException(){
        super(message);
    }
}
