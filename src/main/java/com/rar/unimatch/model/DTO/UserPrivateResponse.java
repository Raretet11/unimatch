package com.rar.unimatch.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Вся приватная информация о пользователе")
public record UserPrivateResponse(
    @Schema(description = "Email", example = "example@example.com")
    String email
) {}
