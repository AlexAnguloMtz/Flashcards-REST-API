package com.aram.flashcards.auth.exception;

public class AppException extends RuntimeException {

    public AppException() {}

    public AppException(String message) {
        super(message);
    }
}
