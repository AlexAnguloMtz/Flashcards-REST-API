package com.aram.flashcards.core.service;

import com.aram.flashcards.core.dto.StudySessionCreationRequest;
import com.aram.flashcards.core.model.StudySession;

public interface StudySessionService {

    StudySession save(StudySessionCreationRequest request);

}
