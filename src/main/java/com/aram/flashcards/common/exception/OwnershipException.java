package com.aram.flashcards.common.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class OwnershipException extends BaseException {

    public OwnershipException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return FORBIDDEN;
    }

}
