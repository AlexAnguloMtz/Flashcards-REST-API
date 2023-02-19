package com.aram.flashcards.core.data;

import com.aram.flashcards.core.dto.CategoryCreationRequest;
import com.aram.flashcards.core.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoriesLoader implements CommandLineRunner {

    @Autowired
    private CategoryService categoryService;

    @Override
    public void run(String... args) throws Exception {
        categoryNames().stream()
            .map(CategoryCreationRequest::new)
            .forEach(categoryService::save);
    }

    private List<String> categoryNames() {
        return List.of(
                "Art",
                "Biology",
                "Chemistry",
                "Engineering",
                "Finance",
                "Graphic Design",
                "Languages",
                "Math",
                "Medicine",
                "Music",
                "Programming",
                "Sports"
        );
    }

}