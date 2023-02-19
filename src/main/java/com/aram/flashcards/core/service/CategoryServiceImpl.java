package com.aram.flashcards.core.service;

import com.aram.flashcards.core.repository.CategoryRepository;
import com.aram.flashcards.core.dto.CategoryCreationRequest;
import com.aram.flashcards.core.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private IdGenerator idGenerator;

    @Override
    @Transactional
    public Category save(CategoryCreationRequest creationRequest) {
        return categoryRepository.save(categoryFrom(creationRequest));
    }

    private Category categoryFrom(CategoryCreationRequest creationRequest) {
        return new Category(nextId(), creationRequest.getName());
    }

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    private String nextId() {
        return idGenerator.nextId();
    }

}
