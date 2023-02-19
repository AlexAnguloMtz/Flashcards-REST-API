package com.aram.flashcards.core.dto;

import jakarta.validation.constraints.Pattern;

public record CategoryCreationRequest(
        @Pattern(regexp = "^[A-Za-z\\s]{1,30}$", message = "{error.category.name}") String name
) {}