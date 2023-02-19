package com.aram.flashcards.core.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class IdGenerator {

    String nextId() {
        return UUID.randomUUID().toString();
    }

}
