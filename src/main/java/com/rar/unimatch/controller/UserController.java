package com.rar.unimatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rar.unimatch.model.DTO.UserPatchRequest;
import com.rar.unimatch.model.DTO.UserPrivateResponse;
import com.rar.unimatch.model.DTO.UserPublicResponse;
import com.rar.unimatch.model.mapper.UserMapper;
import com.rar.unimatch.service.UserService;
import com.rar.unimatch.utils.APIResponse400;
import com.rar.unimatch.utils.APIResponse429;
import com.rar.unimatch.utils.APIResponse500;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(
        summary = "Получение публичной информации о пользователе по id"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Успешное получение информации о пользователе",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UserPublicResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Пользователь с указанным id не найден"
    )
    @APIResponse429
    @APIResponse500
    @GetMapping("/{id}")
    @CircuitBreaker(name = "database")
    @Retry(name = "database")
    public UserPublicResponse getUserById(@PathVariable Long id) {
        return userMapper.toPublicResponse(userService.getById(id));
    }

    @Operation(
        summary = "Получение чувствительной информации о пользователе по токену"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Успешное получение информации о пользователе",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UserPrivateResponse.class)
        )
    )
    @APIResponse429
    @APIResponse500
    @GetMapping("/me")
    @CircuitBreaker(name = "database")
    @Retry(name = "database")
    public UserPrivateResponse getUserInfoByToken() {
        return userMapper.toPrivateResponse(userService.getCurrentUser());
    }

    @Operation(
        summary = "Обновление параметров пользователя по токену"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Возвращает обновленного пользователя",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UserPublicResponse.class)
        )
    )
    @APIResponse400
    @APIResponse429
    @APIResponse500
    @PatchMapping("/me")
    @CircuitBreaker(name = "database")
    @Retry(name = "database")
    public UserPublicResponse patchUserInfo(@RequestBody UserPatchRequest request) {
        return userMapper.toPublicResponse(userService.patchUserParams(request.updates, userService.getCurrentUser()));
    }
}
