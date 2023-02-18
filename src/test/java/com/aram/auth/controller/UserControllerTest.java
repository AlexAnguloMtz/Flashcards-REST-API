package com.aram.auth.controller;

import com.aram.auth.dto.AuthResponse;
import com.aram.auth.dto.LoginRequest;
import com.aram.auth.dto.SignupRequest;
import com.aram.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private UserService userService;

    @Test
    void returnsOkWithJwtWhenUserSignsUpWithValidCredentials() throws Exception {
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(validRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").exists());
    }

    @Test
    void returnsUnprocessableEntityAndMessageWhenUsernameIsTooShort() throws Exception {
        var request = new SignupRequest("min", validEmail(), validPassword(), admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"Username must be between 4 - 20 characters and must contain only letters, digits and underscore\"}"));
    }

    @Test
    void returnsUnprocessableEntityAndMessageWhenUsernameIsTooLong() throws Exception {
        var request = new SignupRequest("this_username_is_huge", validEmail(), validPassword(), admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"Username must be between 4 - 20 characters and must contain only letters, digits and underscore\"}"));
    }

    @Test
    void returnsUnprocessableEntityAndMessageWhenUsernameContainsInvalidSymbols() throws Exception {
        var request = new SignupRequest("username_!#$%&/", validEmail(), validPassword(), admin());
        mockMvc.perform(post(signupPath())
               .contentType(APPLICATION_JSON)
               .content(json(request)))
               .andExpect(status().isUnprocessableEntity())
               .andExpect(content()
               .json("{\"error\":\"Username must be between 4 - 20 characters and must contain only letters, digits and underscore\"}"));
    }

    @Test
    void returnsUnprocessableEntityAndMessageWhenEmailIsInvalid() throws Exception {
        var request = new SignupRequest(validUsername(), "invalid_gmail.com", validPassword(), admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\"error\":\"Invalid email\"}"));
    }

    @Test
    void returnsUnprocessableEntityAndMessageWhenPasswordIsTooShort() throws Exception {
        var request = new SignupRequest(validUsername(), validEmail(), "Short9#", admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"%s\"}".formatted(invalidPasswordMessage())));
    }

    @Test
    void returnsUnprocessableEntityAndMessageWhenPasswordDoesNotHaveDigits() throws Exception {
        var request = new SignupRequest(validUsername(), validEmail(), "NoDigitsHere##", admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"%s\"}".formatted(invalidPasswordMessage())));
    }

    @Test
    void returnsUnprocessableEntityAndMessageWhenPasswordDoesNotHaveUppercase() throws Exception {
        var request = new SignupRequest(validUsername(), validEmail(), "nouppercase99##", admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"%s\"}".formatted(invalidPasswordMessage())));
    }

    @Test
    void returnsUnprocessableEntityAndMessageWhenPasswordDoesNotHaveLowercase() throws Exception {
        var request = new SignupRequest(validUsername(), validEmail(), "NOLOWERCASE99##", admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"%s\"}".formatted(invalidPasswordMessage())));
    }

    @Test
    void returnsUnprocessableEntityAndMessageWhenPasswordDoesNotHaveSymbols() throws Exception {
        var request = new SignupRequest(validUsername(), validEmail(), "NoSymbols9999", admin());
        mockMvc.perform(post(signupPath())
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                .json("{\"error\":\"%s\"}".formatted(invalidPasswordMessage())));
    }

    @Test
    void returnsConflictWhenUsernameAlreadyExists() throws Exception {
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
    void returnsConflictWhenEmailAlreadyExists() throws Exception {
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
    void returnsNotFoundWhenRoleDoesNotExist() throws Exception {
        var request = new SignupRequest(validUsername(), validEmail(), validPassword(), "INVALID_ROLE");
        mockMvc.perform(post(signupPath())
               .contentType(APPLICATION_JSON)
               .content(json(request)))
               .andExpect(status().isNotFound());
    }

    @Test
    void returnsOkWithJwtWhenUserLogsInWithUsername() throws Exception {
        signup(new SignupRequest("daniel", validEmail(), validPassword(), admin()));
        mockMvc.perform(post(loginPath())
               .contentType(APPLICATION_JSON)
               .content(json(new LoginRequest("daniel", validPassword()))))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.jwt").exists());
    }

    @Test
    void returnsOkWithJwtWhenUserLogsInWithEmail() throws Exception {
        signup(new SignupRequest(validUsername(), "daniel@gmail.com", validPassword(), admin()));
        mockMvc.perform(post(loginPath())
               .contentType(APPLICATION_JSON)
               .content(json(new LoginRequest("daniel@gmail.com", validPassword()))))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.jwt").exists());
    }

    @Test
    void returnsUnauthorizedAndMessageWhenUserLogsInWithNonExistentUsername() throws Exception {
        signup(new SignupRequest(validUsername(), validEmail(), validPassword(), admin()));
        mockMvc.perform(post(loginPath())
               .contentType(APPLICATION_JSON)
               .content(json(new LoginRequest("daniel", validPassword()))))
               .andExpect(status().isUnauthorized())
               .andExpect(content().json("{\"error\":\"Invalid credentials\"}"));
    }

    @Test
    void returnsUnauthorizedAndMessageWhenUserLogsInWithNonExistentEmail() throws Exception {
        signup(new SignupRequest(validUsername(), validEmail(), validPassword(), admin()));
        mockMvc.perform(post(loginPath())
               .contentType(APPLICATION_JSON)
               .content(json(new LoginRequest("daniel@gmail.com", validPassword()))))
               .andExpect(status().isUnauthorized())
               .andExpect(content().json("{\"error\":\"Invalid credentials\"}"));
    }

    @Test
    void returnsUnauthorizedAndMessageWhenUserLogsInWithIncorrectPassword() throws Exception {
        signup(new SignupRequest(validUsername(), validEmail(), validPassword(), admin()));
        mockMvc.perform(post(loginPath())
               .contentType(APPLICATION_JSON)
               .content(json(new LoginRequest(validUsername(), "IncorrectPassword99##"))))
               .andExpect(status().isUnauthorized())
               .andExpect(content().json("{\"error\":\"Invalid credentials\"}"));
    }

    @Test
    void returnsForbiddenWithMessageWhenCallingSecuredEndpointWithoutAuthorizationHeader() throws Exception {
        mockMvc.perform(get("/users/hello")
               .contentType(APPLICATION_JSON))
               .andExpect(status().isForbidden())
               .andExpect(content().json("{\"error\":\"Cannot access secured endpoint without valid jwt\"}"));
    }

    @Test
    void returnsForbiddenWithMessageWhenCallingSecuredEndpointWithEmptyToken() throws Exception {
        mockMvc.perform(get("/users/hello")
               .header(AUTHORIZATION, "Bearer ")
               .contentType(APPLICATION_JSON))
               .andExpect(status().isForbidden())
               .andExpect(content().json("{\"error\":\"Cannot access secured endpoint without valid jwt\"}"));
    }

    @Test
    void returnsForbiddenWithMessageWhenCallingSecuredEndpointWithFakeToken() throws Exception {
        mockMvc.perform(get("/users/hello")
               .header(AUTHORIZATION, "Bearer ThisTokenIsFake")
               .contentType(APPLICATION_JSON))
               .andExpect(status().isForbidden())
               .andExpect(content().json("{\"error\":\"Cannot access secured endpoint without valid jwt\"}"));
    }

    @Test
    void returnsForbiddenWithMessageWhenCallingSecuredEndpointWithAlteredToken() throws Exception {
        String validToken = token(signup(validRequest()));
        mockMvc.perform(get("/users/hello")
               .header(AUTHORIZATION, "Bearer %s".formatted(validToken + "A"))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isForbidden())
               .andExpect(content().json("{\"error\":\"Cannot access secured endpoint without valid jwt\"}"));
    }

    @Test
    void returnsOkWithResponseBodyWhenCallingSecuredEndpointWithValidJwt() throws Exception {
        String validToken = token(signup(validRequest()));
        mockMvc.perform(get("/users/hello")
               .header(AUTHORIZATION, "Bearer %s".formatted(validToken))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string("Hello"));
    }

    @Test
    void adminUsersHaveAccessToAdminResources() throws Exception {
        String adminToken = token(signup(newUserWithRole(admin())));
        mockMvc.perform(get("/users")
               .header(AUTHORIZATION, "Bearer %s".formatted(adminToken))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isOk());
    }

    @Test
    void regularUsersDoNotHaveAccessToAdminResources() throws Exception {
        String regularUserToken = token(signup(newUserWithRole(regularUser())));
        mockMvc.perform(get("/users")
               .header(AUTHORIZATION, "Bearer %s".formatted(regularUserToken))
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

    private SignupRequest newUserWithRole(String roleName) {
        return new SignupRequest(
                validUsername(),
                validEmail(),
                validPassword(),
                roleName
        );
    }

    public String validUsername() {
        return "lewis";
    }

    private String validEmail() {
        return "some_email@gmail.com";
    }

    private String admin() {
        return "ROLE_ADMIN";
    }

    private String regularUser() {
        return "ROLE_USER";
    }

    private String validPassword() {
        return "Password99##";
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

    private AuthResponse signup(SignupRequest request) {
        return userService.signup(request);
    }

    private String token(AuthResponse response) {
        return response.getJwt();
    }

}