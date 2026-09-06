package com.saraiva.biblioteca.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Biblioteca Saraiva API",
                version = "1.0",
                description = "REST API for managing books and authors in a personal library."
        )
)
public class OpenApiConfig {
}
