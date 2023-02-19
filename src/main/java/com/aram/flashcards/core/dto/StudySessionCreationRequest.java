package com.aram.flashcards.core.dto;

import lombok.Data;

@Data
public class StudySessionCreationRequest {
    private final String name;
    private final String categoryId;
    private final String userId;
}