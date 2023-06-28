package com.br.equaly.auth.app.controller;

import com.br.equaly.auth.app.exception.CorporationNotFoundException;
import com.br.equaly.auth.app.exception.DepartmentNotFoundException;
import com.br.equaly.auth.app.exception.InvalidTokenException;
import com.br.equaly.auth.app.exception.UserValidationException;
import com.br.equaly.auth.app.model.dto.error.ErrorHandlerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler({CorporationNotFoundException.class, DepartmentNotFoundException.class, LockedException.class, DisabledException.class, UserValidationException.class, InvalidTokenException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorHandlerDTO authenticationError(Exception e){
        return new ErrorHandlerDTO(HttpStatus.UNAUTHORIZED, e);
    }
}
