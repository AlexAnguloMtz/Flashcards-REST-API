package com.aram.flashcards.core.service;

import com.aram.flashcards.core.dto.FlashcardCreationRequest;
import com.aram.flashcards.core.dto.FlashcardResponse;
import com.aram.flashcards.core.dto.FlashcardUpdateRequest;

public interface FlashcardService {

    Iterable<FlashcardResponse> findAllByStudySessionId(String studySessionId);

    FlashcardResponse save(FlashcardCreationRequest request);

    FlashcardResponse update(String id, FlashcardUpdateRequest request);

    void deleteById(String id);
}
