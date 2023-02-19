package com.aram.flashcards.common;

import com.aram.flashcards.auth.configuration.CustomAuthenticationToken;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

public abstract class AbstractService {

    public String authenticatedUserId() {
        var authentication = (CustomAuthenticationToken) getContext().getAuthentication();
        return authentication.getId();
    }

}
