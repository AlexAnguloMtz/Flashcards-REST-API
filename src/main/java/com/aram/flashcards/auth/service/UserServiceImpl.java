package com.aram.flashcards.auth.service;

import com.aram.flashcards.auth.dto.LoginRequest;
import com.aram.flashcards.auth.dto.SignupRequest;
import com.aram.flashcards.auth.dto.AuthResponse;
import com.aram.flashcards.auth.exception.AppException;
import com.aram.flashcards.auth.exception.ConflictException;
import com.aram.flashcards.auth.exception.InvalidCredentialsException;
import com.aram.flashcards.auth.exception.NotFoundException;
import com.aram.flashcards.auth.model.AppRole;
import com.aram.flashcards.auth.model.AppUser;
import com.aram.flashcards.auth.repository.UserRepository;
import com.aram.flashcards.auth.configuration.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RoleService roleService;

    @Override
    public AuthResponse signup(SignupRequest request) {
        if (exists(userFrom(request))) {
            throw new ConflictException();
        }
        save(userFrom(request));
        return responseFrom(userFrom(request));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        AppUser user = findUserFrom(request);
        verifyPasswordsMatch(request, user);
        return responseFrom(user);
    }

    @Override
    public AppUser findByUsernameOrEmail(String usernameOrEmail) {
        return findByUsernameOrEmail(usernameOrEmail, NotFoundException::new);
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

    private void verifyPasswordsMatch(LoginRequest request, AppUser user) {
        if (!doPasswordsMatch(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
    }

    private AppUser findUserFrom(LoginRequest request) {
        return findByUsernameOrEmail(request.getUsernameOrEmail(), InvalidCredentialsException::new);
    }

    private boolean doPasswordsMatch(String requestPassword, String realPassword) {
        return passwordEncoder.matches(requestPassword, realPassword);
    }

    private boolean exists(AppUser user) {
        return userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail());
    }

    private AppUser findByUsernameOrEmail(String usernameOrEmail, Supplier<AppException> exception) {
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
        return new AuthResponse(tokenFor(user));
    }

    private String nextId() {
        return "1";
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
