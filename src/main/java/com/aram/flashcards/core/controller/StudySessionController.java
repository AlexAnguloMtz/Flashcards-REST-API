package com.aram.flashcards.core.controller;

import com.aram.flashcards.core.dto.StudySessionCreationRequest;
import com.aram.flashcards.core.model.StudySession;
import com.aram.flashcards.core.service.StudySessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/study-sessions")
public class StudySessionController {

    @Autowired
    private StudySessionService studySessionService;

    @PostMapping
    public ResponseEntity<StudySession> save(@RequestBody StudySessionCreationRequest request) {
        return ok(studySessionService.save(request));
    }

}