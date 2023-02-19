package com.aram.flashcards.auth.configuration;

import com.aram.flashcards.auth.service.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        log.error("Filtering request in JWT Filter...");
        if (isValidRequest(request)) {
            log.error("Request is valid. Populating security context...");
            populateContextWith(userFrom(request));
        }
        log.error("Passing request to next filter...");
        filterChain.doFilter(request, response);
    }

    private boolean isValidRequest(HttpServletRequest request) {
        return isValidHeader(headerFrom(request)) && isValidToken(tokenFrom(request));
    }

    private boolean isValidHeader(String header) {
        return header != null && header.startsWith(prefix());
    }

    private boolean isValidToken(String token) {
        return tokenProvider.isValidToken(token);
    }

    private String tokenFrom(HttpServletRequest request) {
        return headerFrom(request).substring(prefix().length());
    }

    private String prefix() {
        return "Bearer ";
    }

    private String headerFrom(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION);
    }

    private UserDetails userFrom(HttpServletRequest request) {
        return findByUsername(usernameFrom(tokenFrom(request)));
    }

    private String usernameFrom(String token) {
        return tokenProvider.extractUsername(token);
    }

    private void populateContextWith(UserDetails user) {
        SecurityContextHolder.getContext().setAuthentication(authenticationFrom(user));
    }

    private UserDetails findByUsername(String username) {
        return userDetailsService.loadUserByUsername(username);
    }

    private Authentication authenticationFrom(UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        return new CustomAuthenticationToken(
                customUserDetails.getId(),
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );
    }

}