package com.chamados.API.controllers.common;

import com.chamados.API.controllers.dtos.ResponseError;
import com.chamados.API.exceptions.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        return ResponseError.conflict(e.getMessage());
    }
}
