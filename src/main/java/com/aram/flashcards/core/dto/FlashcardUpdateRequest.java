package com.aram.flashcards.core.dto;

public record FlashcardUpdateRequest (
    String question,
    String answer,
    String studySessionId
) {}