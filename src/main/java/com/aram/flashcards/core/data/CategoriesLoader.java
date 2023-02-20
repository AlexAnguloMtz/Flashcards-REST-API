package com.aram.flashcards.core.data;

import com.aram.flashcards.common.data.JsonLoader;
import com.aram.flashcards.core.dto.CategoryCreationRequest;
import com.aram.flashcards.core.service.CategoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CategoriesLoader implements CommandLineRunner {

    @Value("${json.categories}")
    private String path;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private JsonLoader loader;

    @Override
    public void run(String... args) throws IOException {
        categoriesFrom(path).forEach(categoryService::save);
    }

    private List<CategoryCreationRequest> categoriesFrom(String path) {
        return loader.readJson(path, new TypeReference<>() {});
    }

}