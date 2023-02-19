package com.aram.flashcards.auth.service;

import com.aram.flashcards.auth.dto.LoginRequest;
import com.aram.flashcards.auth.dto.SignupRequest;
import com.aram.flashcards.auth.dto.AuthResponse;
import com.aram.flashcards.auth.model.AppUser;

public interface UserService {

    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);


    AppUser findByUsernameOrEmail(String usernameOrEmail);

    Iterable<AppUser> findAll();

    void deleteByUsername(String username);
}
