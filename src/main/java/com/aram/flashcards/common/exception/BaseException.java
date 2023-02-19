package com.aram.flashcards.common.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {

    public abstract HttpStatus status();

    public BaseException(String message) {
        super(message);
    }

}
