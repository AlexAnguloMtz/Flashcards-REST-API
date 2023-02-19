package com.aram.flashcards.auth.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private final String jwt;
    private final String id;
    private final String username;
    private final String email;
}