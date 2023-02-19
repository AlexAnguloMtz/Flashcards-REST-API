package com.aram.flashcards.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = PRIVATE, force = true)
public class Category {
    @Id
    private final String id;
    private final String name;

    public boolean hasName(String name) {
        return name.equals(name);
    }
}