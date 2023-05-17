package com.aram.flashcards.core.controller;

import com.aram.flashcards.common.controller.ApiErrorMessage;
import com.aram.flashcards.core.dto.FlashcardResponse;
import com.aram.flashcards.core.model.Category;
import com.aram.flashcards.core.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(
            description = "Get all categories",
            summary = "Get categories",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = Category.class)),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            description = "Not an admin / Invalid json web token",
                            responseCode = "403",
                            content = @Content(
                                    schema = @Schema(implementation = ApiErrorMessage.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Iterable<Category>> findAll() {
        return ok(categoryService.findAll());
    }



}