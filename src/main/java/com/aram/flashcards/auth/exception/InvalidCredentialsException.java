package com.aram.flashcards.auth.exception;

import com.aram.flashcards.common.exception.BaseException;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class InvalidCredentialsException extends BaseException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return UNAUTHORIZED;
    }

}