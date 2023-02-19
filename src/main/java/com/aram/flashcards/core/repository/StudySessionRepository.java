package com.aram.flashcards.core.repository;

import com.aram.flashcards.core.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudySessionRepository extends JpaRepository<StudySession, String> {
}
