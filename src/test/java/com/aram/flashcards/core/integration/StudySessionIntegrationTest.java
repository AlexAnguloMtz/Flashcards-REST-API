package com.aram.flashcards.core.integration;

import com.aram.flashcards.AbstractIntegrationTest;
import com.aram.flashcards.auth.dto.AuthResponse;
import com.aram.flashcards.auth.dto.SignupRequest;
import com.aram.flashcards.auth.service.UserService;
import com.aram.flashcards.core.dto.StudySessionCreationRequest;
import com.aram.flashcards.core.dto.StudySessionResponse;
import com.aram.flashcards.core.dto.StudySessionUpdateRequest;
import com.aram.flashcards.core.model.Category;
import com.aram.flashcards.core.model.StudySession;
import com.aram.flashcards.core.service.CategoryService;
import com.aram.flashcards.core.service.StudySessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudySessionIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE_PATH = "/study-sessions";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryService  categoryService;

    @Autowired
    private StudySessionService studySessionService;

    private Category category;

    private AuthResponse authResponse;

    @Autowired
    public StudySessionIntegrationTest(UserService userService) {
        super(userService);
    }

    @BeforeEach
    void init() {
        this.category = findCategoryByName("Music");
        this.authResponse = saveNewUserAndReturnResponse();
    }

    @Test
    void can_create_a_new_study_session() throws Exception {
        var request = new StudySessionCreationRequest(authResponse.getId(), category.getId(), "Guitar chords");

        mockMvc.perform(post(basePath())
                .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId", is(authResponse.getId())))
                .andExpect(jsonPath("$.categoryId", is(category.getId())))
                .andExpect(jsonPath("$.name", is("Guitar chords")))
                .andExpect(jsonPath("$.flashcards").exists());
    }

    @Test
    void cannot_create_a_study_session_for_a_user_that_does_not_exist() throws Exception {
        var request = new StudySessionCreationRequest("Fake User Id", category.getId(), "Guitar chords");

        mockMvc.perform(post(basePath())
               .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
               .contentType(APPLICATION_JSON)
               .content(json(request)))
               .andExpect(status().isNotFound());
    }

    @Test
    void cannot_create_a_study_session_for_a_category_that_does_not_exist() throws Exception {
        var request = new StudySessionCreationRequest(authResponse.getId(), "Fake Category Name", "Guitar chords");

        mockMvc.perform(post(basePath())
               .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
               .contentType(APPLICATION_JSON)
               .content(json(request)))
               .andExpect(status().isNotFound());
    }

    @Test
    void users_can_delete_their_own_study_sessions() throws Exception {
        var request = new StudySessionCreationRequest(authResponse.getId(), category.getId(), "Guitar chords");
        StudySessionResponse studySession = saveStudySession(request);

        mockMvc.perform(delete(basePath() + "/" + studySession.id())
               .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isNoContent());
    }

    @Test
    void users_cannot_delete_study_sessions_that_belong_to_someone_else() throws Exception {
        AuthResponse secondUserAuth = signup(new SignupRequest("second_user", "second@gmail.com", "Password99##", regularUser()));
        var request = new StudySessionCreationRequest(authResponse.getId(), category.getId(), "Guitar chords");
        StudySessionResponse studySession = saveStudySession(request);

        mockMvc.perform(delete(basePath() + "/" + studySession.id())
               .header(AUTHORIZATION, headerWithToken(secondUserAuth.getJwt()))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isForbidden())
               .andExpect(content().json("{\"error\":\"%s\"}".formatted(ownershipErrorMessage())));
    }

    @Test
    void cannot_create_study_session_with_empty_name() throws Exception {
        var request = new StudySessionCreationRequest(authResponse.getId(), category.getId(), "");
        mockMvc.perform(post(basePath())
               .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
               .contentType(APPLICATION_JSON)
               .content(json(request)))
               .andExpect(status().isUnprocessableEntity())
               .andExpect(content().json("{\"error\":\"Request cannot contain empty attributes\"}"));
    }

    @Test
    void users_can_get_all_their_study_sessions() throws Exception {
        Stream.of("Guitar chords", "Flute chords").map(this::requestWithName).forEach(this::saveStudySession);

        mockMvc.perform(get("%s/filter?userId=%s".formatted(basePath(), authResponse.getId()))
               .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    void study_sessions_are_sorted_by_alphabetical_order_by_default() throws Exception {
        Stream.of("Guitar chords", "Flute chords").map(this::requestWithName).forEach(this::saveStudySession);

        mockMvc.perform(get("%s/filter?userId=%s".formatted(basePath(), authResponse.getId()))
               .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name", is("Flute chords")))
               .andExpect(jsonPath("$[1].name", is("Guitar chords")));
    }

    @Test
    void users_can_edit_name_and_category_of_their_own_study_sessions() throws Exception {
        StudySessionResponse savedStudySession = saveStudySession(requestWithName("Guitar chords"));
        String newCategoryId = idFromCategoryWithName("Sports");
        var request = new StudySessionUpdateRequest(newCategoryId, "Water sports");

        var expectedResponse = new StudySession(savedStudySession.id(), "Water sports", authResponse.getId(), newCategoryId);

        mockMvc.perform(put(basePath() + "/" + savedStudySession.id())
                .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(json(expectedResponse)));
    }

    @Test
    void users_cannot_edit_study_sessions_that_belong_to_someone_else() throws Exception {
        AuthResponse secondUser = signup(new SignupRequest("second_user", "second@gmail.com", validPassword(), regularUser()));
        StudySessionResponse savedStudySession = saveStudySession(requestWithName("Guitar chords"));

        var request = new StudySessionUpdateRequest(category.getId(), "Flute chords");

        mockMvc.perform(put(basePath() + "/" + savedStudySession.id())
               .header(AUTHORIZATION, headerWithToken(secondUser.getJwt()))
               .contentType(APPLICATION_JSON)
               .content(json(request)))
               .andExpect(status().isForbidden())
               .andExpect(content().json("{\"error\":\"%s\"}".formatted(ownershipErrorMessage())));
    }

    @Test
    void users_cannot_edit_a_study_session_with_a_non_existent_category() throws Exception {
        StudySessionResponse savedStudySession = saveStudySession(requestWithName("Guitar chords"));
        var request = new StudySessionUpdateRequest("Fake Category Id", savedStudySession.name());

        mockMvc.perform(put(basePath() + "/" + savedStudySession.id())
               .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
               .contentType(APPLICATION_JSON)
               .content(json(request)))
               .andExpect(status().isNotFound())
               .andExpect(content().json("{\"error\":\"Cannot find category with id = Fake Category Id\"}"));
    }

    private StudySessionCreationRequest requestWithName(String studySessionName) {
        return new StudySessionCreationRequest(authResponse.getId(), category.getId(), studySessionName);
    }

    private Category findCategoryByName(String name) {
        return categoryService.findByName(name);
    }

    private String basePath() {
        return BASE_PATH;
    }

    private StudySessionResponse saveStudySession(StudySessionCreationRequest request) {
        return studySessionService.save(request);
    }

    private String ownershipErrorMessage() {
        return "This study session does not belong to the authenticated user";
    }

    private String idFromCategoryWithName(String categoryName) {
        return findCategoryByName(categoryName).getId();
    }
}