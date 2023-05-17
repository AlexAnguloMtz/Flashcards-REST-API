package com.aram.flashcards.common.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Alex Angulo",
                        email = "raulalexo100@gmail.com",
                        url = "https://github.com/AlexAnguloMtz"
                ),
                description = "Flashcards API: An API for managing flashcards and study sessions",
                title = "OpenAPI specification for Flashcards API",
                version = "1.0"
        )
)
class OpenApiConfig {
}
