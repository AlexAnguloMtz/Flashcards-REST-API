package com.aram.flashcards.auth.configuration;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String id;

    public CustomAuthenticationToken(
            String id,
            Object principal,
            Object credentials,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(principal, credentials, authorities);
        this.id = id;
    }

    public String getId() {
        return id;
    }

}