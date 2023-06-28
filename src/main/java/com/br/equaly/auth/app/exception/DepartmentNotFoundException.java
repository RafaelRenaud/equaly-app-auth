package com.br.equaly.auth.app.exception;

public class DepartmentNotFoundException extends RuntimeException{
    private static final String message = "Department not found or inactivated, please contact the system administrator";

    public DepartmentNotFoundException(){
        super(message);
    }
}
