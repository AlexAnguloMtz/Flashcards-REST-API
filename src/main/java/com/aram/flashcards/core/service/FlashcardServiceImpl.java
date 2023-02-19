package com.aram.flashcards.core.service;

import com.aram.flashcards.core.dto.FlashcardResponse;
import com.aram.flashcards.core.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class FlashcardServiceImpl implements FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Override
    public Iterable<FlashcardResponse> findAllByStudySessionId(String studySessionId) {
        return flashcardRepository.findAllByStudySessionId(studySessionId);
    }
}
