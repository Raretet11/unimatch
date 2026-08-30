package com.rar.unimatch.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rar.unimatch.model.DTO.TagCreateRequest;
import com.rar.unimatch.model.DTO.TagFindSimilarRequest;
import com.rar.unimatch.model.DTO.TagPublicResponse;
import com.rar.unimatch.model.mapper.TagMapper;
import com.rar.unimatch.service.TagService;
import com.rar.unimatch.utils.APIErrorResponses;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tags")
@AllArgsConstructor
@APIErrorResponses
public class TagController {
    private final TagService tagService;
    private final TagMapper tagMapper;

    @Operation(
        summary = "Создание тега"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TagPublicResponse.class)
        )
    )
    @PostMapping
    @CircuitBreaker(name = "database")
    @Retry(name = "default")
    public TagPublicResponse createTag(@RequestBody TagCreateRequest request) {
        return tagMapper.toPublicResponse(tagService.create(request));
    }

    @Operation(
        summary = "Поиск похожих по названию тегов"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = TagPublicResponse.class))
        )
    )
    @GetMapping("/search")
    @CircuitBreaker(name = "database")
    @Retry(name = "default")
    public List<TagPublicResponse> findSimilarTag(@RequestBody TagFindSimilarRequest request) {
        return tagService.findSimilar(request.tag(), request.threshold(), request.limit())
                .stream()
                .map(tagMapper::toPublicResponse)
                .collect(Collectors.toList());
    }
}
