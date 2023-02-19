package com.aram.flashcards.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "{error.empty.attribute}")
    private final String usernameOrEmail;

    @NotBlank(message = "{error.empty.attribute}")
    private final String password;

}