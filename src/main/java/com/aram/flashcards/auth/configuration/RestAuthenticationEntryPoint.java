package com.aram.flashcards.auth.configuration;

import com.aram.flashcards.common.controller.ApiErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper serializer;

    public RestAuthenticationEntryPoint() {
        serializer = new ObjectMapper();
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authenticationException)
            throws IOException
    {
        response.setContentType(APPLICATION_JSON.getType());
        response.setStatus(SC_FORBIDDEN);
        response.getOutputStream().println(json(new ApiErrorMessage("Cannot access secured endpoint without valid jwt")));
    }

    private String json(ApiErrorMessage apiErrorMessage) {
        try {
            return serializer.writeValueAsString(apiErrorMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize object: " + apiErrorMessage);
        }
    }

}