package com.rar.unimatch.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rar.unimatch.model.DTO.CommentCreateRequest;
import com.rar.unimatch.model.DTO.CommentPublicResponse;
import com.rar.unimatch.model.DTO.TagPublicResponse;
import com.rar.unimatch.model.mapper.CommentMapper;
import com.rar.unimatch.service.CommentService;
import com.rar.unimatch.service.UserService;
import com.rar.unimatch.utils.APIErrorResponses;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/comments")
@Tag(name = "Comments")
@AllArgsConstructor
@APIErrorResponses
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final UserService userService;

    @Operation(
        summary = "Создание комментария"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CommentPublicResponse.class)
        )
    )
    @PostMapping
    @CircuitBreaker(name = "database")
    @Retry(name = "default")
    public CommentPublicResponse createComment(@RequestBody CommentCreateRequest request) {
        return commentMapper.toPublicResponse(commentService.save(request, userService.getCurrentUser()));
    }
}
