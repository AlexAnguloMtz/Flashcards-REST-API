package com.aram.auth.service;

import com.aram.auth.dto.LoginRequest;
import com.aram.auth.dto.SignupRequest;
import com.aram.auth.dto.AuthResponse;
import com.aram.auth.model.AppUser;

public interface UserService {

    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);


    AppUser findByUsernameOrEmail(String usernameOrEmail);

    Iterable<AppUser> findAll();
}
