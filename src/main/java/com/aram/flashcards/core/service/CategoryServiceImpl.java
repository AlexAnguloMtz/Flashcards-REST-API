package com.aram.flashcards.core.service;

import com.aram.flashcards.common.exception.ConflictException;
import com.aram.flashcards.common.exception.NotFoundException;
import com.aram.flashcards.common.IdGenerator;
import com.aram.flashcards.core.repository.CategoryRepository;
import com.aram.flashcards.core.dto.CategoryCreationRequest;
import com.aram.flashcards.core.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private IdGenerator idGenerator;

    @Override
    @Transactional
    public Category save(CategoryCreationRequest creationRequest) {
        if (existsByName(creationRequest.name())) {
            throw new ConflictException("Category with name = %s already exists".formatted(creationRequest.name()));
        }
        return categoryRepository.save(categoryFrom(creationRequest));
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Could not find category with name = %s".formatted(name)));
    }

    @Override
    public boolean existsById(String categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    private Category categoryFrom(CategoryCreationRequest creationRequest) {
        return new Category(nextId(), creationRequest.name());
    }

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    private String nextId() {
        return idGenerator.nextId();
    }

    private boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

}
