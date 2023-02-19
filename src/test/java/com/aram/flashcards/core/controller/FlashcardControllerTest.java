package com.aram.flashcards.core.controller;

import com.aram.flashcards.AbstractControllerTest;
import com.aram.flashcards.auth.dto.AuthResponse;
import com.aram.flashcards.auth.service.UserService;
import com.aram.flashcards.core.dto.FlashcardCreationRequest;
import com.aram.flashcards.core.dto.StudySessionCreationRequest;
import com.aram.flashcards.core.model.Category;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FlashcardControllerTest extends AbstractControllerTest {

    private static final String BASE_PATH = "/flashcards";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudySessionService studySessionService;

    @Autowired
    private CategoryService categoryService;

    private Category category;

    private AuthResponse authResponse;

    @Autowired
    public FlashcardControllerTest(UserService userService) {
        super(userService);
    }

    @BeforeEach
    void init() {
        this.category = findCategoryByName("Music");
        this.authResponse = saveNewUserAndReturnResponse();
    }

    @Test
    void userCanCreateNewFlashcard() throws Exception {
        String studySessionId = saveStudySessionAndReturnId();
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

    private String basePath() {
        return BASE_PATH;
    }

    private String saveStudySessionAndReturnId() {
        var request = new StudySessionCreationRequest(authResponse.getId(), category.getId(), "Instruments");
        return studySessionService.save(request).id();
    }

    private Category findCategoryByName(String name) {
        return categoryService.findByName(name);
    }


}