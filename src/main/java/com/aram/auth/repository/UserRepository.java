package com.aram.auth.repository;

import com.aram.auth.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Integer> {
    boolean existsByUsernameOrEmail(String username, String email);

    Optional<AppUser> findByUsernameOrEmail(String username, String email);
}
