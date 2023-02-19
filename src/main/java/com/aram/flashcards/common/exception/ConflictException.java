package com.aram.flashcards.common.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

public class ConflictException extends BaseException {

    public ConflictException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return CONFLICT;
    }

}