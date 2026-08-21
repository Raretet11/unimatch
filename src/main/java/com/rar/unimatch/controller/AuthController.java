package com.rar.unimatch.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rar.unimatch.model.DTO.JwtAuthenticationResponse;
import com.rar.unimatch.model.DTO.SignInRequest;
import com.rar.unimatch.model.DTO.SignUpRequest;
import com.rar.unimatch.service.AuthenticationService;
import com.rar.unimatch.utils.APIErrorResponses;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Регистрация")
@AllArgsConstructor
@APIErrorResponses
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(
        summary = "Регистрация нового пользователя",
        description = "email, username должны быть уникальными"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = JwtAuthenticationResponse.class)
        )
    )
    @SecurityRequirements
    @PostMapping("/sign-up")
    @RateLimiter(name = "authRegistration")
    @CircuitBreaker(name = "database")
    @Retry(name = "database")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(
        summary = "Получение токена соответствующего пользователю"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = JwtAuthenticationResponse.class)
        )
    )
    @SecurityRequirements
    @PostMapping("/sign-in")
    @RateLimiter(name = "authLogin")
    @CircuitBreaker(name = "database")
    @Retry(name = "database")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
