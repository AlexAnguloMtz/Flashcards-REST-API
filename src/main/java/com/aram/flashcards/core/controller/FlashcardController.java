package com.aram.flashcards.core.controller;

import com.aram.flashcards.core.dto.FlashcardCreationRequest;
import com.aram.flashcards.core.dto.FlashcardResponse;
import com.aram.flashcards.core.dto.FlashcardUpdateRequest;
import com.aram.flashcards.core.service.FlashcardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/flashcards")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    @PostMapping
    public ResponseEntity<FlashcardResponse> save(@RequestBody @Valid FlashcardCreationRequest request) {
        return new ResponseEntity<>(flashcardService.save(request), CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashcardResponse> update(
            @PathVariable String id,
            @RequestBody FlashcardUpdateRequest request
    ) {
        return new ResponseEntity<>(flashcardService.update(id, request), CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        flashcardService.deleteById(id);
        return noContent().build();
    }

}