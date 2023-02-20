package com.aram.flashcards.core.service;

import com.aram.flashcards.common.exception.NotFoundException;
import com.aram.flashcards.common.AbstractService;
import com.aram.flashcards.common.IdGenerator;
import com.aram.flashcards.common.exception.OwnershipException;
import com.aram.flashcards.core.dto.FlashcardCreationRequest;
import com.aram.flashcards.core.dto.FlashcardResponse;
import com.aram.flashcards.core.dto.FlashcardUpdateRequest;
import com.aram.flashcards.core.model.Flashcard;
import com.aram.flashcards.core.model.StudySession;
import com.aram.flashcards.core.repository.FlashcardRepository;
import com.aram.flashcards.core.repository.StudySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class FlashcardServiceImpl extends AbstractService implements FlashcardService {

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
    @Transactional
    public FlashcardResponse save(FlashcardCreationRequest request) {
        checkPreconditions(request);
        return responseFrom(saveAndReturn(flashcardFrom(request)));
    }

    @Override
    @Transactional
    public FlashcardResponse update(String id, FlashcardUpdateRequest request) {
        checkPreconditions(id, request);
        return responseFrom(saveAndReturn(flashcardFrom(id, request)));
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        if (existsById(id)) {
            checkOwnership(findFlashcardById(id));
            flashcardRepository.deleteById(id);
        }
    }

    private void checkPreconditions(FlashcardCreationRequest request) {
        checkOwnership(findStudySessionById(request.studySessionId()));
    }

    private StudySession findStudySessionById(String id) {
        return studySessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cannot find study session with id = %s".formatted(id)));
    }

    private void checkPreconditions(String id, FlashcardUpdateRequest request) {
        checkOwnership(findFlashcardById(id));
        checkOwnership(findStudySessionById(request.studySessionId()));
    }

    private void checkOwnership(Flashcard flashcard) {
        if (!belongsToAuthenticatedUser(flashcard)) {
            throw new OwnershipException("This flashcard does not belong to the authenticated user");
        }
    }

    private void checkOwnership(StudySession studySession) {
        if (!belongsToAuthenticatedUser(studySession)) {
            throw new OwnershipException("This study session does not belong to the authenticated user");
        }
    }

    private boolean belongsToAuthenticatedUser(Flashcard flashcard) {
        StudySession studySession = findStudySessionById(flashcard.getStudySessionId());
        return studySession.getUserId().equals(authenticatedUserId());
    }

    private boolean belongsToAuthenticatedUser(StudySession studySession) {
        return studySession.getUserId().equals(authenticatedUserId());
    }

    private Flashcard findFlashcardById(String id) {
        return flashcardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cannot find flashcard with id = %s".formatted(id)));
    }

    private Flashcard saveAndReturn(Flashcard flashcard) {
        return flashcardRepository.save(flashcard);
    }

    private FlashcardResponse responseFrom(Flashcard flashcard) {
        return new FlashcardResponse(
                flashcard.getId(),
                flashcard.getQuestion(),
                flashcard.getAnswer(),
                flashcard.getStudySessionId()
        );
    }

    private Flashcard flashcardFrom(FlashcardCreationRequest request) {
        return new Flashcard(
                nextId(),
                request.question(),
                request.answer(),
                request.studySessionId()
        );
    }

    private Flashcard flashcardFrom(String id, FlashcardUpdateRequest request) {
        return new Flashcard(
                id,
                request.question(),
                request.answer(),
                request.studySessionId()
        );
    }

    private String nextId() {
        return idGenerator.nextId();
    }

    private boolean existsById(String id) {
        return flashcardRepository.existsById(id);
    }

}