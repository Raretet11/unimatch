package com.rar.unimatch.model.DTO;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
    @NotBlank
    String username,

    @NotBlank
    String password
) {}
