package com.aram.flashcards.auth.controller;

import com.aram.flashcards.auth.dto.LoginRequest;
import com.aram.flashcards.auth.dto.SignupRequest;
import com.aram.flashcards.auth.service.UserService;
import com.aram.flashcards.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest extends AbstractControllerTest {

    private static final String SIGNUP_PATH = "/users/signup";
    private static final String LOGIN_PATH = "/users/login";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public UserControllerTest(UserService userService) {
        super(userService);
    }

    @Test
    void okWithJwtWhenUserSignsUpWithValidCredentials() throws Exception {
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(validRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").exists());
    }

    @Test
    void unprocessableEntityAndMessageWhenUsernameIsTooShort() throws Exception {
        var request = new SignupRequest("min", validEmail(), validPassword(), admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"Username must be between 4 - 20 characters and must contain only letters, digits and underscore\"}"));
    }

    @Test
    void unprocessableEntityAndMessageWhenUsernameIsTooLong() throws Exception {
        var request = new SignupRequest("this_username_is_huge", validEmail(), validPassword(), admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"Username must be between 4 - 20 characters and must contain only letters, digits and underscore\"}"));
    }

    @Test
    void unprocessableEntityAndMessageWhenUsernameContainsInvalidSymbols() throws Exception {
        var request = new SignupRequest("username_!#$%&/", validEmail(), validPassword(), admin());
        mockMvc.perform(post(signupPath())
               .contentType(APPLICATION_JSON)
               .content(json(request)))
               .andExpect(status().isUnprocessableEntity())
               .andExpect(content()
               .json("{\"error\":\"Username must be between 4 - 20 characters and must contain only letters, digits and underscore\"}"));
    }

    @Test
    void unprocessableEntityAndMessageWhenEmailIsInvalid() throws Exception {
        var request = new SignupRequest(validUsername(), "invalid_gmail.com", validPassword(), admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\"error\":\"Invalid email\"}"));
    }

    @Test
    void unprocessableEntityAndMessageWhenPasswordIsTooShort() throws Exception {
        var request = new SignupRequest(validUsername(), validEmail(), "Short9#", admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"%s\"}".formatted(invalidPasswordMessage())));
    }

    @Test
    void unprocessableEntityAndMessageWhenPasswordDoesNotHaveDigits() throws Exception {
        var request = new SignupRequest(validUsername(), validEmail(), "NoDigitsHere##", admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"%s\"}".formatted(invalidPasswordMessage())));
    }

    @Test
    void unprocessableEntityAndMessageWhenPasswordDoesNotHaveUppercase() throws Exception {
        var request = new SignupRequest(validUsername(), validEmail(), "nouppercase99##", admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"%s\"}".formatted(invalidPasswordMessage())));
    }

    @Test
    void unprocessableEntityAndMessageWhenPasswordDoesNotHaveLowercase() throws Exception {
        var request = new SignupRequest(validUsername(), validEmail(), "NOLOWERCASE99##", admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"%s\"}".formatted(invalidPasswordMessage())));
    }

    @Test
    void unprocessableEntityAndMessageWhenPasswordDoesNotHaveSymbols() throws Exception {
        var request = new SignupRequest(validUsername(), validEmail(), "NoSymbols9999", admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"%s\"}".formatted(invalidPasswordMessage())));
    }

    @Test
    void conflictWhenUsernameAlreadyExists() throws Exception {
        signup(new SignupRequest(
                validRequest().getUsername(),
                "email@gmail.com",
                "Password99##",
                admin()
        ));
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(validRequest())))
                .andExpect(status().isConflict());
    }

    @Test
    void conflictWhenEmailAlreadyExists() throws Exception {
        signup(new SignupRequest(
                "jordan",
                validRequest().getEmail(),
                "Password99##",
                admin()
        ));
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(validRequest())))
                .andExpect(status().isConflict());
    }

    @Test
    void notFoundWhenRoleDoesNotExist() throws Exception {
        var request = new SignupRequest(validUsername(), validEmail(), validPassword(), "INVALID_ROLE");
        mockMvc.perform(post(signupPath())
               .contentType(APPLICATION_JSON)
               .content(json(request)))
               .andExpect(status().isNotFound());
    }

    @Test
    void okWithJwtWhenUserLogsInWithUsername() throws Exception {
        signup(new SignupRequest("daniel", validEmail(), validPassword(), admin()));
        mockMvc.perform(post(loginPath())
               .contentType(APPLICATION_JSON)
               .content(json(new LoginRequest("daniel", validPassword()))))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.jwt").exists());
    }

    @Test
    void okWithJwtWhenUserLogsInWithEmail() throws Exception {
        signup(new SignupRequest(validUsername(), "daniel@gmail.com", validPassword(), admin()));
        mockMvc.perform(post(loginPath())
               .contentType(APPLICATION_JSON)
               .content(json(new LoginRequest("daniel@gmail.com", validPassword()))))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.jwt").exists());
    }

    @Test
    void unauthorizedAndMessageWhenUserLogsInWithNonExistentUsername() throws Exception {
        signup(new SignupRequest(validUsername(), validEmail(), validPassword(), admin()));
        mockMvc.perform(post(loginPath())
               .contentType(APPLICATION_JSON)
               .content(json(new LoginRequest("daniel", validPassword()))))
               .andExpect(status().isUnauthorized())
               .andExpect(content().json("{\"error\":\"Invalid credentials\"}"));
    }

    @Test
    void unauthorizedAndMessageWhenUserLogsInWithNonExistentEmail() throws Exception {
        signup(new SignupRequest(validUsername(), validEmail(), validPassword(), admin()));
        mockMvc.perform(post(loginPath())
               .contentType(APPLICATION_JSON)
               .content(json(new LoginRequest("daniel@gmail.com", validPassword()))))
               .andExpect(status().isUnauthorized())
               .andExpect(content().json("{\"error\":\"Invalid credentials\"}"));
    }

    @Test
    void unauthorizedAndMessageWhenUserLogsInWithIncorrectPassword() throws Exception {
        signup(new SignupRequest(validUsername(), validEmail(), validPassword(), admin()));
        mockMvc.perform(post(loginPath())
               .contentType(APPLICATION_JSON)
               .content(json(new LoginRequest(validUsername(), "IncorrectPassword99##"))))
               .andExpect(status().isUnauthorized())
               .andExpect(content().json("{\"error\":\"Invalid credentials\"}"));
    }

    @Test
    void forbiddenWithMessageWhenCallingSecuredEndpointWithoutAuthorizationHeader() throws Exception {
        mockMvc.perform(get("/users/hello")
               .contentType(APPLICATION_JSON))
               .andExpect(status().isForbidden())
               .andExpect(content().json("{\"error\":\"Cannot access secured endpoint without valid jwt\"}"));
    }

    @Test
    void forbiddenWithMessageWhenCallingSecuredEndpointWithEmptyToken() throws Exception {
        mockMvc.perform(get("/users/hello")
               .header(AUTHORIZATION, "Bearer ")
               .contentType(APPLICATION_JSON))
               .andExpect(status().isForbidden())
               .andExpect(content().json("{\"error\":\"Cannot access secured endpoint without valid jwt\"}"));
    }

    @Test
    void forbiddenWithMessageWhenCallingSecuredEndpointWithFakeToken() throws Exception {
        mockMvc.perform(get("/users/hello")
               .header(AUTHORIZATION, "Bearer ThisTokenIsFake")
               .contentType(APPLICATION_JSON))
               .andExpect(status().isForbidden())
               .andExpect(content().json("{\"error\":\"Cannot access secured endpoint without valid jwt\"}"));
    }

    @Test
    void forbiddenWithMessageWhenCallingSecuredEndpointWithAlteredToken() throws Exception {
        String validToken = token(signup(validRequest()));
        mockMvc.perform(get("/users/hello")
               .header(AUTHORIZATION, bearerPrefix().formatted(validToken + "A"))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isForbidden())
               .andExpect(content().json("{\"error\":\"Cannot access secured endpoint without valid jwt\"}"));
    }

    @Test
    void okWithResponseBodyWhenCallingSecuredEndpointWithValidJwt() throws Exception {
        String validToken = token(signup(validRequest()));
        mockMvc.perform(get("/users/hello")
               .header(AUTHORIZATION, bearerPrefix().formatted(validToken))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string("Hello"));
    }

    @Test
    void adminUsersHaveAccessToAdminResources() throws Exception {
        String adminToken = token(signup(newUserWithRole(admin())));
        mockMvc.perform(get("/users")
               .header(AUTHORIZATION, bearerPrefix().formatted(adminToken))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isOk());
    }

    @Test
    void regularUsersDoNotHaveAccessToAdminResources() throws Exception {
        String regularUserToken = token(signup(newUserWithRole(regularUser())));
        mockMvc.perform(get("/users")
               .header(AUTHORIZATION, bearerPrefix().formatted(regularUserToken))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isForbidden());
    }

    @Test
    void usersCanDeleteTheirOwnAccount() throws Exception {
        SignupRequest signupRequest = newUserWithRole(regularUser());
        String token = token(signup(signupRequest));
        mockMvc.perform(delete("/users/%s".formatted(signupRequest.getUsername()))
               .header(AUTHORIZATION, bearerPrefix().formatted(token))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isNoContent());
    }

    @Test
    void usersCannotDeleteAccountThatDoesNotBelongToThem() throws Exception {
        var firstUserRequest = new SignupRequest("lewis", "lewis@gmail.com", "Password99##", regularUser());
        var secondUserRequest = new SignupRequest("daniel", "daniel@gmail.com", "Password55!!", regularUser());
        signup(firstUserRequest);
        String secondUserToken = token(signup(secondUserRequest));

        mockMvc.perform(delete("/users/%s".formatted(firstUserRequest.getUsername()))
               .header(AUTHORIZATION, bearerPrefix().formatted(secondUserToken))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isForbidden());
    }

    private SignupRequest validRequest() {
        return new SignupRequest(
                validUsername(),
                validEmail(),
                validPassword(),
                admin()
        );
    }

    private String invalidPasswordMessage() {
        return "Password must contain at least 8 characters, one uppercase, one lowercase, one digit and one symbol";
    }

    private String signupPath() {
        return SIGNUP_PATH;
    }

    private String loginPath() {
        return LOGIN_PATH;
    }

}