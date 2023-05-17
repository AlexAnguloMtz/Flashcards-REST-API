package com.aram.flashcards.core.controller;

import com.aram.flashcards.common.controller.ApiErrorMessage;
import com.aram.flashcards.core.dto.FlashcardCreationRequest;
import com.aram.flashcards.core.dto.FlashcardResponse;
import com.aram.flashcards.core.dto.FlashcardUpdateRequest;
import com.aram.flashcards.core.service.FlashcardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/flashcards")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    @Operation(
            description = "Endpoint to save a flashcard",
            summary = "Save flashcard",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = FlashcardResponse.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            description = "Specified study session does not belong to the logged user / Invalid json web token",
                            responseCode = "403",
                            content = @Content(
                                    schema = @Schema(implementation = ApiErrorMessage.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            description = "Request is missing some required information",
                            responseCode = "422",
                            content = @Content(
                                    schema = @Schema(implementation = ApiErrorMessage.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<FlashcardResponse> save(@RequestBody @Valid FlashcardCreationRequest request) {
        return new ResponseEntity<>(flashcardService.save(request), CREATED);
    }

    @Operation(
            description = "Endpoint to update a flashcard",
            summary = "Update flashcard",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = FlashcardResponse.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            description = "Specified study session does not belong to the logged user / Invalid json web token",
                            responseCode = "403",
                            content = @Content(
                                    schema = @Schema(implementation = ApiErrorMessage.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            description = "Request is missing some required information",
                            responseCode = "422",
                            content = @Content(
                                    schema = @Schema(implementation = ApiErrorMessage.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<FlashcardResponse> update(
            @PathVariable String id,
            @RequestBody FlashcardUpdateRequest request
    ) {
        return new ResponseEntity<>(flashcardService.update(id, request), CREATED);
    }

    @Operation(
            description = "Endpoint to delete a flashcard",
            summary = "Delete flashcard",
            responses = {
                    @ApiResponse(
                            description = "Flashcard was deleted successfully",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Logged user does not own this resource / Invalid json web token",
                            responseCode = "403",
                            content = @Content(
                                    schema = @Schema(implementation = ApiErrorMessage.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        flashcardService.deleteById(id);
        return noContent().build();
    }

}