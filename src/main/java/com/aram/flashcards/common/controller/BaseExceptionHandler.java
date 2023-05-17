package com.aram.flashcards.common.controller;

import com.aram.flashcards.common.exception.BaseException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiErrorMessage> handle(BaseException exception) {
        return new ResponseEntity<>(new ApiErrorMessage(exception.getMessage()), exception.status());
    }

    @Hidden
    @ResponseBody
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorMessage handle(MethodArgumentNotValidException exception) {
        return new ApiErrorMessage(firstErrorMessageFrom(exception));
    }

    private String firstErrorMessageFrom(MethodArgumentNotValidException exception) {
        return exception
                .getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();
    }

}