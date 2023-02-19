package com.aram.flashcards.core.service;

import com.aram.flashcards.common.exception.NotFoundException;
import com.aram.flashcards.auth.service.UserService;
import com.aram.flashcards.common.AbstractService;
import com.aram.flashcards.common.IdGenerator;
import com.aram.flashcards.common.exception.OwnershipException;
import com.aram.flashcards.core.dto.FlashcardResponse;
import com.aram.flashcards.core.dto.StudySessionCreationRequest;
import com.aram.flashcards.core.dto.StudySessionResponse;
import com.aram.flashcards.core.dto.StudySessionUpdateRequest;
import com.aram.flashcards.core.model.StudySession;
import com.aram.flashcards.core.repository.FlashcardRepository;
import com.aram.flashcards.core.repository.StudySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

import static java.util.Comparator.comparing;

@Service
class StudySessionServiceImpl extends AbstractService implements StudySessionService {

    @Autowired
    private StudySessionRepository studySessionRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private FlashcardRepository flashcardRepository;

    @Override
    @Transactional
    public StudySessionResponse save(StudySessionCreationRequest request) {
        checkPreconditions(request);
        return responseFrom(saveAndReturn(studySessionFrom(request)));
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        checkOwnership(findById(id));
        studySessionRepository.deleteById(id);
    }

    @Override
    public Iterable<StudySessionResponse> filter(Map<String, String> parameters) {
        return findAllByUserId(parameters.get("userId")).stream()
                .map(this::responseFrom)
                .sorted(comparing(StudySessionResponse::name))
                .toList();
    }

    @Override
    @Transactional
    public StudySessionResponse update(String id, StudySessionUpdateRequest request) {
        checkOwnership(findById(id));
        checkCategoryExistsById(request.categoryId());
        return responseFrom(saveAndReturn(studySessionFrom(id, request)));
    }

    @Override
    public boolean existsById(String id) {
        return studySessionRepository.existsById(id);
    }

    private void checkOwnership(StudySession studySession) {
        if (!studySession.getUserId().equals(authenticatedUserId())) {
            throw new OwnershipException("This study session does not belong to the authenticated user");
        }
    }

    private StudySession findById(String id) {
        return studySessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not find study session with id = %s".formatted(id)));
    }

    private void checkPreconditions(StudySessionCreationRequest request) {
        checkUserExistsById(request.userId());
        checkCategoryExistsById(request.categoryId());
    }

    private void checkUserExistsById(String userId) {
        if (!userExistsById(userId)) {
            throw new NotFoundException("Cannot find user with id = %s".formatted(userId));
        }
    }

    private void checkCategoryExistsById(String categoryId) {
        if (!categoryExistsById(categoryId)) {
            throw new NotFoundException("Cannot find category with id = %s".formatted(categoryId));
        }
    }

    private boolean categoryExistsById(String categoryId) {
        return categoryService.existsById(categoryId);
    }

    private boolean userExistsById(String userId) {
        return userService.existsById(userId);
    }

    private StudySession studySessionFrom(StudySessionCreationRequest request) {
        return new StudySession(
                nextId(),
                request.name(),
                request.userId(),
                request.categoryId()
        );
    }

    private StudySession studySessionFrom(String id, StudySessionUpdateRequest request) {
        return new StudySession(
                id,
                request.name(),
                authenticatedUserId(),
                request.categoryId()
        );
    }

    private String nextId() {
        return idGenerator.nextId();
    }

    private StudySessionResponse responseFrom(StudySession studySession) {
        return new StudySessionResponse(
                studySession.getId(),
                studySession.getName(),
                studySession.getUserId(),
                studySession.getCategoryId(),
                findFlashcardsByStudySessionId(studySession.getId())
        );
    }

    private Iterable<FlashcardResponse> findFlashcardsByStudySessionId(String id) {
        return flashcardRepository.findAllByStudySessionId(id);
    }

    private StudySession saveAndReturn(StudySession studySession) {
        return studySessionRepository.save(studySession);
    }

    private Collection<StudySession> findAllByUserId(String userId) {
        return studySessionRepository.findByUserId(userId);
    }

}