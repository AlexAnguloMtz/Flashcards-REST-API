package com.aram.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractControllerTest {

    private final ObjectMapper serializer;

    public AbstractControllerTest() {
        serializer = new ObjectMapper();
    }

    public String json(Object object) {
        try {
            return serializer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize");
        }
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return serializer.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to read json " + json);
        }
    }

}
