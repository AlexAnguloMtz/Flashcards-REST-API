package com.aram.flashcards.auth.controller;

import com.aram.flashcards.auth.dto.LoginRequest;
import com.aram.flashcards.auth.dto.SignupRequest;
import com.aram.flashcards.auth.dto.AuthResponse;
import com.aram.flashcards.auth.model.AppUser;
import com.aram.flashcards.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody @Valid SignupRequest request) {
        return ok(userService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ok(userService.login(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable<AppUser>> findAll() {
        return ok(userService.findAll());
    }

    // This endpoint is to unit test this project.
    // Should be removed when adding this project to an actual production app.
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<?> delete(@PathVariable String username) {
        userService.deleteByUsername(username);
        return noContent().build();
    }

}