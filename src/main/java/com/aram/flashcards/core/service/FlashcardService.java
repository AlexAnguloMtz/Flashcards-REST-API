package com.aram.flashcards.core.service;

import com.aram.flashcards.core.dto.FlashcardResponse;

public interface FlashcardService {

    Iterable<FlashcardResponse> findAllByStudySessionId(String studySessionId);

}
