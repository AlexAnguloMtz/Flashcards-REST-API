package com.aram.flashcards.core.service;

import com.aram.flashcards.core.dto.StudySessionCreationRequest;
import com.aram.flashcards.core.dto.StudySessionResponse;
import com.aram.flashcards.core.dto.StudySessionUpdateRequest;

import java.util.Map;

public interface StudySessionService {

    StudySessionResponse save(StudySessionCreationRequest request);

    void deleteById(String id);

    Iterable<StudySessionResponse> findAllByUserId(String userId);

    StudySessionResponse update(String id, StudySessionUpdateRequest request);

    boolean existsById(String id);
}