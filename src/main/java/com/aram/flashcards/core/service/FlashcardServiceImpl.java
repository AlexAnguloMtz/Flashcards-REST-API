package com.aram.flashcards.core.service;

import com.aram.flashcards.auth.exception.NotFoundException;
import com.aram.flashcards.common.IdGenerator;
import com.aram.flashcards.core.dto.FlashcardCreationRequest;
import com.aram.flashcards.core.dto.FlashcardResponse;
import com.aram.flashcards.core.model.Flashcard;
import com.aram.flashcards.core.repository.FlashcardRepository;
import com.aram.flashcards.core.repository.StudySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class FlashcardServiceImpl implements FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public Iterable<FlashcardResponse> findAllByStudySessionId(String studySessionId) {
        return flashcardRepository.findAllByStudySessionId(studySessionId);
    }

    @Override
    public FlashcardResponse save(FlashcardCreationRequest request) {
        checkPreconditions(request);
        return responseFrom(saveAndReturn(flashcardFrom(request)));
    }

    private void checkPreconditions(FlashcardCreationRequest request) {
        if (!studySessionExistsById(request.studySessionId())) {
            throw new NotFoundException("Cannot find study session with id = %s".formatted(request.studySessionId()));
        }
    }

    private boolean studySessionExistsById(String studySessionId) {
        return studySessionRepository.existsById(studySessionId);
    }

    private FlashcardResponse responseFrom(Flashcard flashcard) {
        return new FlashcardResponse(
                flashcard.getId(),
                flashcard.getQuestion(),
                flashcard.getAnswer(),
                flashcard.getStudySessionId()
        );
    }

    private Flashcard saveAndReturn(Flashcard flashcard) {
        return flashcardRepository.save(flashcard);
    }

    private Flashcard flashcardFrom(FlashcardCreationRequest request) {
        return new Flashcard(
                nextId(),
                request.question(),
                request.answer(),
                request.studySessionId()
        );
    }

    private String nextId() {
        return idGenerator.nextId();
    }


}
