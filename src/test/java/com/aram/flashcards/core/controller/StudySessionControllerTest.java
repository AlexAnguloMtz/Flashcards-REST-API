package com.aram.flashcards.core.controller;

import com.aram.flashcards.AbstractControllerTest;
import com.aram.flashcards.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StudySessionControllerTest extends AbstractControllerTest {

    private static final String STUDY_SESSIONS_PATH = "/study-sessions";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public StudySessionControllerTest(UserService userService) {
        super(userService);
    }

    @Test
    void okWithAllStudySessionsOwnedByUser() throws Exception {
        mockMvc.perform(get(studySessionsPath())
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, ""))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    private String studySessionsPath() {
        return STUDY_SESSIONS_PATH;
    }

}
