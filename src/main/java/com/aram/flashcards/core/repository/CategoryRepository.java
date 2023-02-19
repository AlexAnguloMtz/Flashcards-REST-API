package com.aram.flashcards.core.repository;

import com.aram.flashcards.core.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
