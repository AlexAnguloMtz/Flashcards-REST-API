package com.aram.flashcards.auth.repository;

import com.aram.flashcards.auth.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, String> {

    boolean existsByUsernameOrEmail(String username, String email);

    Optional<AppUser> findByUsernameOrEmail(String username, String email);

    void deleteByUsername(String username);

}
