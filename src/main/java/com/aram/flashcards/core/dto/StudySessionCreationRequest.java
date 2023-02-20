package com.aram.flashcards.core.dto;

import jakarta.validation.constraints.NotBlank;

public record StudySessionCreationRequest(

        @NotBlank(message = "{error.empty.attribute}")
        String userId,

        @NotBlank(message = "{error.empty.attribute}")
        String categoryId,

        @NotBlank(message = "{error.empty.attribute}")
        String name
) {}