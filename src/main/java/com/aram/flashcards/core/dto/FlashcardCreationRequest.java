package com.aram.flashcards.core.dto;

import jakarta.validation.constraints.NotBlank;

public record FlashcardCreationRequest(

        @NotBlank(message = "{error.empty.attribute}")
        String question,

        @NotBlank(message = "{error.empty.attribute}")
        String answer,

        @NotBlank(message = "{error.empty.attribute}")
        String studySessionId

) {}