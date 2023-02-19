package com.aram.flashcards.core.dto;

public record StudySessionResponse(
        String id,
        String name,
        String userId,
        String categoryId,
        Iterable<FlashcardResponse> flashcards
) {}