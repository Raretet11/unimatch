package com.rar.unimatch.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rar.unimatch.model.DTO.PatchRequest;
import com.rar.unimatch.model.DTO.UserLinkCreateRequest;
import com.rar.unimatch.model.DTO.UserLinkPublicResponse;
import com.rar.unimatch.model.DTO.UserPrivateResponse;
import com.rar.unimatch.model.DTO.UserPublicResponse;
import com.rar.unimatch.model.mapper.UserMapper;
import com.rar.unimatch.service.UserService;
import com.rar.unimatch.utils.APIErrorResponses;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users")
@AllArgsConstructor
@APIErrorResponses
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(
        summary = "Получение публичной информации о пользователе по id"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UserPublicResponse.class)
        )
    )
    @GetMapping("/{id}")
    @CircuitBreaker(name = "database")
    @Retry(name = "default")
    public UserPublicResponse getUserById(@PathVariable Long id) {
        return userMapper.toPublicResponse(userService.getById(id));
    }

    @Operation(
        summary = "Получение чувствительной информации о пользователе по токену"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UserPrivateResponse.class)
        )
    )
    @GetMapping("/me")
    @CircuitBreaker(name = "database")
    @Retry(name = "default")
    public UserPrivateResponse getUserInfoByToken() {
        return userMapper.toPrivateResponse(userService.getCurrentUser());
    }

    @Operation(
        summary = "Обновление параметров пользователя по токену"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UserPublicResponse.class)
        )
    )
    @PatchMapping("/me")
    @CircuitBreaker(name = "database")
    @Retry(name = "default")
    public UserPublicResponse patchUserInfo(@RequestBody PatchRequest request) {
        return userMapper.toPublicResponse(userService.patchUserParams(request.updates, userService.getCurrentUser()));
    }

    @Operation(
        summary = "Добавление ссылки пользователю"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UserPublicResponse.class)
        )
    )
    @PostMapping("/links")
    @CircuitBreaker(name = "database")
    @Retry(name = "default")
    public UserLinkPublicResponse addUserLink(@RequestBody UserLinkCreateRequest request) {
        return userMapper.toPublicResponse(userService.createUserLink(request, userService.getCurrentUser()));
    }

    @Operation(
        summary = "Получение ссылок пользователя"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = UserLinkPublicResponse.class))
        )
    )
    @GetMapping("/{id}/links")
    @CircuitBreaker(name = "database")
    @Retry(name = "default")
    public List<UserLinkPublicResponse> getUserLinks(@PathVariable Long id) {
        return userService.getLinks(id)
            .stream()
            .map(userMapper::toPublicResponse)
            .collect(Collectors.toList());
    }
}
