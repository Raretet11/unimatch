package com.rar.unimatch.model.mapper;

import org.springframework.stereotype.Component;

import com.rar.unimatch.model.DTO.SkillPublicResponse;
import com.rar.unimatch.model.skill.Skill;

@Component
public class SkillMapper {
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
}
