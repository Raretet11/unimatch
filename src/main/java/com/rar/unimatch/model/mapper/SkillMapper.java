package com.rar.unimatch.model.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.rar.unimatch.model.DTO.SkillPublicResponse;
import com.rar.unimatch.model.skill.Skill;
import com.rar.unimatch.model.skill.SkillSearchDocument;

import lombok.AllArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class SkillMapper {
    private final ObjectMapper objectMapper;

    public SkillPublicResponse toPublicResponse(Skill skill) {
        if (skill == null) {
            return null;
        }

        return new SkillPublicResponse(
            skill.getId(),
            skill.isResponse(),
            skill.getTitle(),
            skill.getDescription(),
            skill.getStudyFormat(),
            skill.getSessionType(),
            skill.getRewardType(),
            skill.getRewardAmountRub(),
            skill.isActive()
        );
    }

    public String toJson(SkillSearchDocument document) {
        try {
            return objectMapper.writeValueAsString(document);
        } catch (JacksonException e) {
            throw new RuntimeException("Failed to convert skill to json", e);
        }
    }

    public List<String> toJson(List<SkillSearchDocument> documents) {
        return documents.stream()
            .map(this::toJson)
            .collect(Collectors.toList());
    }
}
