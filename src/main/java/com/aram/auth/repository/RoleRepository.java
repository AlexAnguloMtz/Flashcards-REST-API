package com.aram.auth.repository;

import com.aram.auth.model.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<AppRole, String> {
    Optional<AppRole> findByName(String name);
}
