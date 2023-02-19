package com.aram.flashcards.core.repository;

import com.aram.flashcards.core.dto.FlashcardResponse;
import com.aram.flashcards.core.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashcardRepository extends JpaRepository<Flashcard, String> {
    Iterable<FlashcardResponse> findAllByStudySessionId(String studySessionId);
}