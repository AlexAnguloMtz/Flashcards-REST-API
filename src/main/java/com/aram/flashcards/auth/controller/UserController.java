package com.aram.flashcards.auth.controller;

import com.aram.flashcards.auth.dto.LoginRequest;
import com.aram.flashcards.auth.dto.SignupRequest;
import com.aram.flashcards.auth.dto.AuthResponse;
import com.aram.flashcards.auth.model.AppUser;
import com.aram.flashcards.auth.service.UserService;
import com.aram.flashcards.common.controller.ApiErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            description = "Endpoint to register new users (Available roles = ROLE_ADMIN, ROLE_USER)",
            summary = "Signup",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AuthResponse.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            description = "Role does not exist",
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = ApiErrorMessage.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            description = "Username or email already exist",
                            responseCode = "409",
                            content = @Content(
                                    schema = @Schema(implementation = ApiErrorMessage.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            description = "Invalid username, email or password",
                            responseCode = "422",
                            content = @Content(
                                    schema = @Schema(implementation = ApiErrorMessage.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody @Valid SignupRequest request) {
        return ok(userService.signup(request));
    }

    @Operation(
            description = "Endpoint to allow users to log in",
            summary = "Log in",
            responses = {
                @ApiResponse(
                        description = "Success",
                        responseCode = "200",
                        content = @Content(
                                schema = @Schema(implementation = AuthResponse.class),
                                mediaType = APPLICATION_JSON_VALUE
                        )
                ),
                @ApiResponse(
                        description = "Invalid credentials",
                        responseCode = "403",
                        content = @Content(
                                schema = @Schema(implementation = ApiErrorMessage.class),
                                mediaType = APPLICATION_JSON_VALUE
                        )
                )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ok(userService.login(request));
    }

    @Operation(
            description = "Endpoint to get all users",
            summary = "Get all users",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = AppUser.class)),
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
                    ),
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable<AppUser>> findAll() {
        return ok(userService.findAll());
    }

    @Operation(
            description = "Endpoint to check if server is up and running",
            summary = "Check endpoint",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
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
                    )
            }
    )
    @GetMapping("/check")
    public String hello() {
        return "Server is up and running";
    }

    @Operation(
            description = "Endpoint to delete a user by username",
            summary = "Delete user",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            description = "User was deleted successfully",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Target user is not the logged user / Invalid json web token",
                            responseCode = "403",
                            content = @Content(
                                    schema = @Schema(implementation = ApiErrorMessage.class),
                                    mediaType = APPLICATION_JSON_VALUE
                            )
                    )
            }
    )
    @DeleteMapping("/{username}")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<?> delete(@PathVariable String username) {
        userService.deleteByUsername(username);
        return noContent().build();
    }

}