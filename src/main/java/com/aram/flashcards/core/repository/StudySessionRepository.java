package com.aram.flashcards.core.repository;

import com.aram.flashcards.core.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface StudySessionRepository extends JpaRepository<StudySession, String> {
    Collection<StudySession> findByUserId(String userId);
}
