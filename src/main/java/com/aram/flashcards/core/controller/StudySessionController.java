package com.aram.flashcards.core.controller;

import com.aram.flashcards.core.dto.StudySessionCreationRequest;
import com.aram.flashcards.core.dto.StudySessionResponse;
import com.aram.flashcards.core.dto.StudySessionUpdateRequest;
import com.aram.flashcards.core.model.StudySession;
import com.aram.flashcards.core.service.StudySessionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/study-sessions")
@Slf4j
public class StudySessionController {

    @Autowired
    private StudySessionService studySessionService;

    @GetMapping("/filter")
    public ResponseEntity<Iterable<StudySessionResponse>> filter(@RequestParam Map<String, String> parameters) {
        return ok(studySessionService.filter(parameters));
    }

    @PostMapping
    public ResponseEntity<StudySessionResponse> save(@RequestBody @Valid StudySessionCreationRequest request) {
        return new ResponseEntity<>(studySessionService.save(request), CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudySessionResponse> update(
            @RequestBody @Valid StudySessionUpdateRequest request,
            @PathVariable String id
    ) {
        return ok(studySessionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StudySession> delete(@PathVariable String id) {
        studySessionService.deleteById(id);
        return noContent().build();
    }

}