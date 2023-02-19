package com.aram.flashcards.auth.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {

    String getId();

}
