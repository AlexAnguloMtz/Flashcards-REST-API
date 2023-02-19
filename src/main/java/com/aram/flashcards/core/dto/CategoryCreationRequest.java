package com.aram.flashcards.core.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CategoryCreationRequest {

    @Pattern(regexp = "^[A-Za-z\\s]{1,20}$", message = "{error.category.name}")
    private final String name;

}