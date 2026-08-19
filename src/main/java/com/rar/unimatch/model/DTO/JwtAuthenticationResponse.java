package com.rar.unimatch.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "JwtAuthenticationResponse",
    description = "JWT токен"
)
public record JwtAuthenticationResponse(
    String token
) {}
