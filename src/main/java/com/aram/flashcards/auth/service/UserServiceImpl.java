package com.aram.flashcards.auth.service;

import com.aram.flashcards.auth.dto.LoginRequest;
import com.aram.flashcards.auth.dto.SignupRequest;
import com.aram.flashcards.auth.dto.AuthResponse;
import com.aram.flashcards.common.exception.BaseException;
import com.aram.flashcards.common.exception.ConflictException;
import com.aram.flashcards.auth.exception.InvalidCredentialsException;
import com.aram.flashcards.common.exception.NotFoundException;
import com.aram.flashcards.auth.model.AppRole;
import com.aram.flashcards.auth.model.AppUser;
import com.aram.flashcards.auth.repository.UserRepository;
import com.aram.flashcards.auth.configuration.TokenProvider;
import com.aram.flashcards.common.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
@Slf4j
class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RoleService roleService;
    @Autowired
    private IdGenerator idGenerator;

    @Override
    public AuthResponse signup(SignupRequest request) {
        if (existsByUsernameOrEmail(userFrom(request))) {
            throw new ConflictException("User with these credentials already exists");
        }
        AppUser user = userFrom(request);
        return responseFrom(save(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        AppUser user = findUserFrom(request);
        verifyPasswordsMatch(request, user);
        return responseFrom(user);
    }

    @Override
    public AppUser findByUsernameOrEmail(String usernameOrEmail) {
        return findByUsernameOrEmail(usernameOrEmail,
                () -> new NotFoundException("Could not find user with credentials = %s".formatted(usernameOrEmail)));
    }

    @Override
    public Iterable<AppUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public boolean existsById(String userId) {
        return userRepository.existsById(userId);
    }

    private void verifyPasswordsMatch(LoginRequest request, AppUser user) {
        if (!doPasswordsMatch(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    private AppUser findUserFrom(LoginRequest request) {
        return findByUsernameOrEmail(request.getUsernameOrEmail(), () -> new InvalidCredentialsException("Invalid credentials"));
    }

    private boolean doPasswordsMatch(String requestPassword, String realPassword) {
        return passwordEncoder.matches(requestPassword, realPassword);
    }

    private boolean existsByUsernameOrEmail(AppUser user) {
        return userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail());
    }

    private AppUser findByUsernameOrEmail(String usernameOrEmail, Supplier<BaseException> exception) {
        return userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(exception);
    }

    @Transactional
    private AppUser save(AppUser user) {
        return userRepository.save(user);
    }

    private AppUser userFrom(SignupRequest request) {
        return new AppUser(
            nextId(),
            request.getUsername(),
            request.getEmail(),
            encodePassword(request.getPassword()),
            findRoleByName(request.getRole())
        );
    }

    private AuthResponse responseFrom(AppUser user) {
        return new AuthResponse(
                tokenFor(user),
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    private String nextId() {
        return idGenerator.nextId();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String tokenFor(AppUser user) {
        return tokenProvider.tokenFor(user);
    }

    private AppRole findRoleByName(String roleName) {
        return roleService.findByName(roleName);
    }

}
