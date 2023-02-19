package com.aram.flashcards.core.service;

import com.aram.flashcards.core.dto.StudySessionCreationRequest;
import com.aram.flashcards.core.model.StudySession;
import com.aram.flashcards.core.repository.StudySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class StudySessionServiceImpl implements StudySessionService {

    @Autowired
    private StudySessionRepository studySessionRepository;
    @Autowired
    private IdGenerator idGenerator;

    @Override
    public StudySession save(StudySessionCreationRequest request) {
        return studySessionRepository.save(studySessionFrom(request));
    }

    private StudySession studySessionFrom(StudySessionCreationRequest request) {
        return new StudySession(
                nextId(),
                request.getName(),
                request.getUserId(),
                request.getCategoryId()
        );
    }

    private String nextId() {
        return idGenerator.nextId();
    }

}