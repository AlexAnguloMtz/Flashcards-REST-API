package com.aram.flashcards.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignupRequest {

    @Pattern(regexp = "^[A-Za-z0-9_]{4,20}$", message = "{error.auth.invalid.username}")
    private final String username;

    @Email(message = "{error.auth.invalid.email}")
    private final String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,20}$",
             message = "{error.auth.invalid.password}")
    private final String password;

    private final String role;

}
