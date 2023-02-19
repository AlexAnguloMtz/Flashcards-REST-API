package com.aram.flashcards.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private final String usernameOrEmail;
    private final String password;
}