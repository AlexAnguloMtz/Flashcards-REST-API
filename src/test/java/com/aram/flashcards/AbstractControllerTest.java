package com.aram.flashcards;

import com.aram.flashcards.auth.dto.AuthResponse;
import com.aram.flashcards.auth.dto.SignupRequest;
import com.aram.flashcards.auth.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.util.List;

public class AbstractControllerTest {

    private final ObjectMapper serializer;
    private final UserService userService;

    public AbstractControllerTest(UserService userService) {
        this.userService = userService;
        serializer = new ObjectMapper();
    }

    protected SignupRequest newUserWithRole(String roleName) {
        return new SignupRequest(
                validUsername(),
                validEmail(),
                validPassword(),
                roleName
        );
    }

    protected String validUsername() {
        return "lewis";
    }

    protected String validEmail() {
        return "some_email@gmail.com";
    }


    protected String validPassword() {
        return "Password99##";
    }

    protected String admin() {
        return "ROLE_ADMIN";
    }

    protected String regularUser() {
        return "ROLE_USER";
    }

    protected AuthResponse signup(SignupRequest request) {
        return userService.signup(request);
    }

    protected String token(AuthResponse response) {
        return response.getJwt();
    }

    protected String bearerPrefix() {
        return "Bearer %s";
    }

    protected String json(Object object) {
        try {
            return serializer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize");
        }
    }

    protected  <T> T fromJson(String json, Class<T> clazz) {
        try {
            return serializer.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to parse json " + json);
        }
    }

    protected  <T> List<T> listFromJson(String json, Class<T> clazz) {
        try {
            ObjectReader reader = serializer.readerForListOf(clazz);
            return reader.readValue(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not parse json list: %s".formatted(json));
        }
    }

}