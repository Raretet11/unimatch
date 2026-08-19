package com.rar.unimatch.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rar.unimatch.error.ErrorResponse;
import com.rar.unimatch.model.DTO.JwtAuthenticationResponse;
import com.rar.unimatch.model.DTO.SignInRequest;
import com.rar.unimatch.model.DTO.SignUpRequest;
import com.rar.unimatch.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Регистрация")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(
        summary = "Регистрация нового пользователя",
        description = "email, username должны быть уникальными"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Успешная регистрация, возвращает JWT токен",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtAuthenticationResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Невалидные входные данные",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
        }
    )
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(
        summary = "Получение токена соответствующего пользователю"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Успешный вход, возвращает JWT токен",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtAuthenticationResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Невалидные входные данные / неверный пароль",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
        }
    )
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
