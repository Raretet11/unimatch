package com.rar.unimatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Unimatch API")
                        .version("1.0.0")
                        .description("API documentation for Unimatch app.")
                        .contact(new Contact().name("Developer").email("MagicRaretet@gmail.com")));
    }
}
