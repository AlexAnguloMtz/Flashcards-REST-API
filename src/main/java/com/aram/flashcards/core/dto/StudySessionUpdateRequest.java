package com.aram.flashcards.core.dto;

public record StudySessionUpdateRequest(
        String categoryId,
        String name
) {}
