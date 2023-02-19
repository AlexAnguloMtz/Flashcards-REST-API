package com.aram.flashcards.core.dto;

public record FlashcardResponse(
        String id,
        String question,
        String answer,
        String studySessionId
) {}