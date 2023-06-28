package com.br.equaly.auth.app.exception;

public class InvalidTokenException extends RuntimeException{
    private static final String message = "Incompatible Tokens";

    public InvalidTokenException(){
        super(message);
    }
}
