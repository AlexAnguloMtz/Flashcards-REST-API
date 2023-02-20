package com.aram.flashcards.core.integration;

import com.aram.flashcards.AbstractIntegrationTest;
import com.aram.flashcards.auth.service.UserService;
import com.aram.flashcards.core.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CategoryIntegrationTest extends AbstractIntegrationTest {

    private static final String CATEGORIES_PATH = "/categories";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public CategoryIntegrationTest(UserService userService) {
        super(userService);
    }

    @Test
    void okWithAllCategories() throws Exception {
        String token = saveNewUserAndReturnToken();
        String response = mockMvc.perform(get(categoriesPath())
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, headerWithToken(token)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Category> categories = listFromJsonArray(response, Category.class);

        assertEquals(12, categories.size());
        assertTrue(hasCategoryWithName(categories, "Programming"));
    }

    private boolean hasCategoryWithName(List<Category> categories, String categoryName) {
        return categories.stream().anyMatch(category -> category.hasName(categoryName));
    }

    private String categoriesPath() {
        return CATEGORIES_PATH;
    }

}