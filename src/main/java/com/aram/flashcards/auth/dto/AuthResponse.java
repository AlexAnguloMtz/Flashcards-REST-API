package com.aram.flashcards.auth.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private final String jwt;
}
