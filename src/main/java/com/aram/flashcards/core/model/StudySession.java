package com.aram.flashcards.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = PRIVATE, force = true)
public class StudySession {

    @Id
    private final String id;
    private final String name;
    private final String userId;
    private final String categoryId;

}