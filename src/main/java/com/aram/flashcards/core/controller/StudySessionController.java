package com.aram.flashcards.core.controller;

import com.aram.flashcards.common.controller.ApiErrorMessage;
import com.aram.flashcards.core.dto.StudySessionCreationRequest;
import com.aram.flashcards.core.dto.StudySessionResponse;
import com.aram.flashcards.core.dto.StudySessionUpdateRequest;
import com.aram.flashcards.core.model.StudySession;
import com.aram.flashcards.core.service.StudySessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/study-sessions")
public class StudySessionController {

    @Autowired
    private StudySessionService studySessionService;

    @Operation(
            description = "Endpoint to get all study sessions by user id",
            summary = "Get study sessions by user id",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = StudySessionResponse.class)),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            description = "Invalid json web token",
                            responseCode = "403",
                            content = @Content(
                                    schema = @Schema(implementation = ApiErrorMessage.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
            }
    )
    @GetMapping
    public ResponseEntity<Iterable<StudySessionResponse>> findAllByUserId(@RequestParam(name = "userId") String userId) {
        return ok(studySessionService.findAllByUserId(userId));
    }

    @Operation(
            description = "Endpoint to save a study session",
            summary = "Save study session",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = StudySessionResponse.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            description = "Invalid json web token",
                            responseCode = "403",
                            content = @Content(
                                    schema = @Schema(implementation = ApiErrorMessage.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            description = "Specified category does not exist",
                            responseCode = "404",
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
    public ResponseEntity<StudySessionResponse> save(@RequestBody @Valid StudySessionCreationRequest request) {
        return new ResponseEntity<>(studySessionService.save(request), CREATED);
    }

    @Operation(
            description = "Endpoint to update a study session",
            summary = "Update study session",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = StudySessionResponse.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            description = "Logged user does not own this resource / Invalid json web token",
                            responseCode = "403",
                            content = @Content(
                                    schema = @Schema(implementation = ApiErrorMessage.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            description = "Specified category does not exist",
                            responseCode = "404",
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
    public ResponseEntity<StudySessionResponse> update(
            @RequestBody @Valid StudySessionUpdateRequest request,
            @PathVariable String id
    ) {
        return ok(studySessionService.update(id, request));
    }

    @Operation(
            description = "Endpoint to delete a study session",
            summary = "Delete study session",
            responses = {
                    @ApiResponse(
                            description = "Study session was deleted successfully",
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
    public ResponseEntity<StudySession> delete(@PathVariable String id) {
        studySessionService.deleteById(id);
        return noContent().build();
    }

}