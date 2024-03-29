package com.aram.flashcards.core.integration;

import com.aram.flashcards.AbstractIntegrationTest;
import com.aram.flashcards.auth.dto.AuthResponse;
import com.aram.flashcards.auth.dto.SignupRequest;
import com.aram.flashcards.auth.service.UserService;
import com.aram.flashcards.core.dto.FlashcardCreationRequest;
import com.aram.flashcards.core.dto.FlashcardUpdateRequest;
import com.aram.flashcards.core.dto.StudySessionCreationRequest;
import com.aram.flashcards.core.model.Category;
import com.aram.flashcards.core.model.Flashcard;
import com.aram.flashcards.core.repository.FlashcardRepository;
import com.aram.flashcards.core.service.CategoryService;
import com.aram.flashcards.core.service.StudySessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FlashcardIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE_PATH = "/flashcards";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudySessionService studySessionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FlashcardRepository flashcardRepository;

    private Category category;

    private AuthResponse authResponse;

    @Autowired
    public FlashcardIntegrationTest(UserService userService) {
        super(userService);
    }

    @BeforeEach
    void init() {
        this.category = findCategoryByName("Music");
        this.authResponse = saveNewUserAndReturnResponse();
    }

    @Test
    void can_create_a_new_flashcard() throws Exception {
        String studySessionId = saveStudySessionAndReturnId(authResponse.getId(), "Instruments");
        var request = new FlashcardCreationRequest("Instrument with 6 strings?", "Guitar", studySessionId);
        mockMvc.perform(post(basePath())
                .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.question", is("Instrument with 6 strings?")))
                .andExpect(jsonPath("$.answer", is("Guitar")))
                .andExpect(jsonPath("$.studySessionId", is(studySessionId)));
    }

    @Test
    void cannot_create_flashcard_with_empty_question() throws Exception {
        String studySessionId = saveStudySessionAndReturnId(authResponse.getId(), "Instruments");
        var request = new FlashcardCreationRequest("", "Guitar", studySessionId);
        mockMvc.perform(post(basePath())
               .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
               .contentType(APPLICATION_JSON)
               .content(json(request)))
               .andExpect(status().isUnprocessableEntity())
               .andExpect(content().json("{\"error\":\"Request cannot contain empty attributes\"}"));
    }

    @Test
    void cannot_create_flashcard_with_empty_answer() throws Exception {
        String studySessionId = saveStudySessionAndReturnId(authResponse.getId(), "Instruments");
        var request = new FlashcardCreationRequest("Instrument with 6 strings?", "", studySessionId);
        mockMvc.perform(post(basePath())
               .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
               .contentType(APPLICATION_JSON)
               .content(json(request)))
               .andExpect(status().isUnprocessableEntity())
               .andExpect(content().json("{\"error\":\"Request cannot contain empty attributes\"}"));
    }

    @Test
    void users_cannot_create_flashcards_for_a_study_session_that_does_not_belong_to_them() throws Exception {
        AuthResponse secondUserAuth = signup(new SignupRequest("second_user", "second@gmail.com", "Password99##", regularUser()));
        String studySessionId = saveStudySessionAndReturnId(authResponse.getId(), "Instruments");
        var request = new FlashcardCreationRequest("Instrument with 6 strings?", "Guitar", studySessionId);

        mockMvc.perform(post(basePath())
                .header(AUTHORIZATION, headerWithToken(secondUserAuth.getJwt()))
                .contentType(APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().json("{\"error\":\"This study session does not belong to the authenticated user\"}"));
    }

    @Test
    void users_can_edit_their_flashcards() throws Exception {
        String firstStudySessionId = saveStudySessionAndReturnId(authResponse.getId(), "Instruments");
        String secondStudySessionId = saveStudySessionAndReturnId(authResponse.getId(), "Musical tones");
        save(new Flashcard("100", "Instrument with 6 strings?", "Guitar", firstStudySessionId));
        var updateRequest = new FlashcardUpdateRequest("First musical note?", "C", secondStudySessionId);

        mockMvc.perform(put(basePath() + "/" + "100")
               .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
               .contentType(APPLICATION_JSON)
               .content(json(updateRequest)))
               .andExpect(status().is2xxSuccessful())
               .andExpect(jsonPath("$.id", is("100")))
               .andExpect(jsonPath("$.question", is("First musical note?")))
               .andExpect(jsonPath("$.answer", is("C")))
               .andExpect(jsonPath("$.studySessionId", is(secondStudySessionId)));
    }

    @Test
    void users_cannot_edit_flashcards_of_a_study_session_that_does_not_belong_to_them() throws Exception {
        AuthResponse secondUserAuth = signup(new SignupRequest("second_user", "second@gmail.com", "Password99##", regularUser()));
        String firstStudySessionId = saveStudySessionAndReturnId(authResponse.getId(), "Instruments");
        String secondStudySessionId = saveStudySessionAndReturnId(secondUserAuth.getId(), "Musical tones");
        save(new Flashcard("100", "Instrument with 6 strings?", "Guitar", firstStudySessionId));
        var updateRequest = new FlashcardUpdateRequest("First musical note?", "C", secondStudySessionId);

        mockMvc.perform(put(basePath() + "/" + "100")
                .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
                .contentType(APPLICATION_JSON)
                .content(json(updateRequest)))
                .andExpect(status().isForbidden())
                .andExpect(content().json("{\"error\":\"This study session does not belong to the authenticated user\"}"));
    }

    @Test
    void users_cannot_edit_flashcards_that_do_not_belong_to_them() throws Exception {
        AuthResponse secondUserAuth = signup(new SignupRequest("second_user", "second@gmail.com", "Password99##", regularUser()));
        String studySessionId = saveStudySessionAndReturnId(authResponse.getId(), "Instruments");
        save(new Flashcard("100", "Instrument with 6 strings?", "Guitar", studySessionId));
        var updateRequest = new FlashcardUpdateRequest("First musical note?", "C", studySessionId);

        mockMvc.perform(put(basePath() + "/" + "100")
               .header(AUTHORIZATION, headerWithToken(secondUserAuth.getJwt()))
               .contentType(APPLICATION_JSON)
               .content(json(updateRequest)))
               .andExpect(status().isForbidden())
               .andExpect(content().json("{\"error\":\"This flashcard does not belong to the authenticated user\"}"));
    }

    @Test
    void users_can_delete_their_flashcards() throws Exception {
        String studySessionId = saveStudySessionAndReturnId(authResponse.getId(), "Instruments");
        save(new Flashcard("100", "Instrument with 6 strings?", "Guitar", studySessionId));
        mockMvc.perform(delete(basePath() + "/" + "100")
               .header(AUTHORIZATION, headerWithToken(authResponse.getJwt()))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isNoContent());
    }

    @Test
    void users_cannot_delete_flashcards_that_belong_to_someone_else() throws Exception {
        AuthResponse secondUserAuth = signup(new SignupRequest("second_user", "second@gmail.com", "Password99##", regularUser()));
        String studySessionId = saveStudySessionAndReturnId(authResponse.getId(), "Instruments");
        save(new Flashcard("100", "Instrument with 6 strings?", "Guitar", studySessionId));

        mockMvc.perform(delete(basePath() + "/" + "100")
               .header(AUTHORIZATION, headerWithToken(secondUserAuth.getJwt()))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isForbidden())
               .andExpect(content().json("{\"error\":\"This flashcard does not belong to the authenticated user\"}"));
    }

    private String basePath() {
        return BASE_PATH;
    }

    private String saveStudySessionAndReturnId(String userId, String name) {
        var request = new StudySessionCreationRequest(userId, category.getId(), name);
        return studySessionService.save(request).id();
    }

    private Category findCategoryByName(String name) {
        return categoryService.findByName(name);
    }

    private Flashcard save(Flashcard flashcard) {
        return flashcardRepository.save(flashcard);
    }

}