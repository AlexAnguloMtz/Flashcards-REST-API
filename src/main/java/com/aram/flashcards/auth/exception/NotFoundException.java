package com.aram.flashcards.auth.exception;

public class NotFoundException extends AppException {

    public NotFoundException() {}

    public NotFoundException(String message) {
        super(message);
    }
}
