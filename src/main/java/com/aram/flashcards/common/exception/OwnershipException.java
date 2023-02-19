package com.aram.flashcards.common.exception;

import com.aram.flashcards.auth.exception.AppException;

public class OwnershipException extends AppException {
    public OwnershipException(String message) {
        super(message);
    }
}
