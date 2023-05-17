package com.aram.flashcards.common.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Alex Angulo",
                        email = "raulalexo100@gmail.com",
                        url = "https://github.com/AlexAnguloMtz"
                ),
                description = "Flashcards API: An API for managing flashcards and study sessions",
                title = "Flashcards API",
                version = "1.0"
        ),
        servers = {@Server(url = "https://flashcards-rest-api-production.up.railway.app", description = "Default Server URL")}
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT token authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
class OpenApiConfig {
}
