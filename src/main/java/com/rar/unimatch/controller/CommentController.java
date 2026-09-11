package com.rar.unimatch.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rar.unimatch.model.DTO.CommentCreateRequest;
import com.rar.unimatch.model.DTO.CommentPublicResponse;
import com.rar.unimatch.model.comment.RatingSummary;
import com.rar.unimatch.model.mapper.CommentMapper;
import com.rar.unimatch.service.CommentService;
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

    @Operation(summary = "Удалить только комментарий с подветкой")
    @DeleteMapping("/{id}")
    @CircuitBreaker(name = "database")
    @Retry(name = "default")
    public void deleteComment(@PathVariable long id) {
        commentService.delete(id);
    }

    @Operation(summary = "Получить информацию о рейтинге по пользователю")
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = RatingSummary.class)
        )
    )
    @GetMapping("/info/{id}")
    @CircuitBreaker(name = "database")
    @Retry(name = "default")
    public RatingSummary getInfo(@PathVariable long id) {
        return commentService.getRatingSummary(id);
    }

    @Operation(summary = "Получить все комментарии к пользователю")
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = CommentPublicResponse.class))
        )
    )
    @GetMapping("/{id}")
    @CircuitBreaker(name = "database")
    @Retry(name = "default")
    public List<CommentPublicResponse> getByUserId(@PathVariable long id) {
        return commentService.getByUser(id)
            .stream()
            .map(commentMapper::toPublicResponse)
            .collect(Collectors.toList());
    }
}
