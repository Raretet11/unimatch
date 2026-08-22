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
import com.rar.unimatch.model.DTO.SkillCreateRequest;
import com.rar.unimatch.model.DTO.SkillPublicResponse;
import com.rar.unimatch.model.DTO.TagAddRequest;
import com.rar.unimatch.model.DTO.TagFindSimilarRequest;
import com.rar.unimatch.model.DTO.TagPublicResponse;
import com.rar.unimatch.model.mapper.SkillMapper;
import com.rar.unimatch.model.mapper.TagMapper;
import com.rar.unimatch.model.tag.SkillTag;
import com.rar.unimatch.service.SkillService;
import com.rar.unimatch.service.SkillTagMapService;
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
@RequestMapping("/api/v1/skills")
@Tag(name = "Skills")
@AllArgsConstructor
@APIErrorResponses
public class SkillController {
    private final SkillService skillService;
    private final UserService userService;
    private final SkillMapper skillMapper;
    private final SkillTagMapService skillTagMapService;

    @Operation(
        summary = "Создание скилла от имени пользователя"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = SkillPublicResponse.class)
        )
    )
    @PostMapping
    @CircuitBreaker(name = "database")
    @Retry(name = "database")
    public SkillPublicResponse createSkill(@RequestBody SkillCreateRequest request) {
        return skillMapper.toPublicResponse(skillService.create(request, userService.getCurrentUser()));
    }

    @Operation(
        summary = "Получение всех скиллов пользователя"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Возвращает список скиллов",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = SkillPublicResponse.class))
        )
    )
    @GetMapping
    @CircuitBreaker(name = "database")
    @Retry(name = "database")
    public List<SkillPublicResponse> getSkillsByUser() {
        return skillService.getSkillsByUser(userService.getCurrentUser())
            .stream()
            .map(skillMapper::toPublicResponse)
            .collect(Collectors.toList());
    }

    @Operation(
        summary = "Обновление параметров пользователя по токену"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = SkillPublicResponse.class)
        )
    )
    @PatchMapping("/{id}")
    @CircuitBreaker(name = "database")
    @Retry(name = "database")
    public SkillPublicResponse patchUserInfo(@RequestBody PatchRequest request, @PathVariable Long id) {
        return skillMapper.toPublicResponse(skillService.patchSkillParams(request.updates, id));
    }

    @Operation(
        summary = "Добавление тегов скиллу с указанным id"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = SkillTag.class))
        )
    )
    @PostMapping("/{id}/tags")
    @CircuitBreaker(name = "database")
    @Retry(name = "database")
    public List<SkillTag> addTags(@PathVariable Long id, @RequestBody TagAddRequest request) {
        return skillTagMapService.addTags(request.tagsId(), id);
    }

    @Operation(
        summary = "Получение всех тегов указанного скилла"
    )
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Long.class))
        )
    )
    @GetMapping("/{id}/tags")
    @CircuitBreaker(name = "database")
    @Retry(name = "database")
    public List<Long> getTags(@PathVariable Long id) {
        return skillTagMapService.getTagsIdBySkillId(id);
    }
}
