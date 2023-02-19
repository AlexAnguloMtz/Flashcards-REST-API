package com.aram.flashcards.core.dto;

public record FlashcardCreationRequest(
        String question,
        String answer,
        String studySessionId
) {}