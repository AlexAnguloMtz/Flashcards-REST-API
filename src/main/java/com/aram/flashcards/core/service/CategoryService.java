package com.aram.flashcards.core.service;

import com.aram.flashcards.core.dto.CategoryCreationRequest;
import com.aram.flashcards.core.model.Category;

public interface CategoryService {

    Iterable<Category> findAll();

    Category save(CategoryCreationRequest categoryCreationRequest);

    Category findByName(String name);

    boolean existsById(String categoryId);
}