package com.aram.flashcards.auth.controller;

import com.aram.flashcards.auth.exception.ConflictException;
import com.aram.flashcards.auth.exception.InvalidCredentialsException;
import com.aram.flashcards.auth.exception.NotFoundException;
import com.aram.flashcards.common.controller.ApiErrorMessage;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class AuthExceptionHandler {

    @ResponseBody
    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public String handle(ConflictException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handle(NotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorMessage handle(MethodArgumentNotValidException exception) {
        return new ApiErrorMessage(firstErrorMessageFrom(exception));
    }

    @ResponseBody
    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(InvalidCredentialsException.class)
    public ApiErrorMessage handle(InvalidCredentialsException exception) {
        return new ApiErrorMessage("Invalid credentials");
    }

    @ResponseBody
    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ApiErrorMessage handle(AuthenticationException exception) {
        return new ApiErrorMessage("Cannot access secured endpoint without valid jwt");
    }

    private String firstErrorMessageFrom(MethodArgumentNotValidException exception) {
        return exception
                .getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();
    }

}