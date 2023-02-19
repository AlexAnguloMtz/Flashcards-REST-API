package com.aram.flashcards.common.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JsonLoader {

    public <T> List<T> readJson(String path, TypeReference<List<T>> typeReference) {
        try (var stream = getInputStream(path)) {
            return readValue(stream, typeReference);
        } catch (Exception exception) {
            throw new RuntimeException("Could not read data from source: %s".formatted(path));
        }
    }

    private <T> List<T> readValue(InputStream stream, TypeReference<List<T>> typeReference) throws IOException {
        return new ObjectMapper().readValue(stream, typeReference);
    }

    private InputStream getInputStream(String path) throws IOException {
        return new ClassPathResource(path).getInputStream();
    }

}