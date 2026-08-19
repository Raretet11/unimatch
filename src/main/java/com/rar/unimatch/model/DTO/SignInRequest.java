package com.rar.unimatch.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
    name = "SignInRequest"
)
public record SignInRequest(
    @NotBlank
    String username,

    @NotBlank
    String password
) {}
