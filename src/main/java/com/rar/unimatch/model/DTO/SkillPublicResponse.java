package com.rar.unimatch.model.DTO;

import java.math.BigDecimal;

import com.rar.unimatch.model.skill.RewardType;
import com.rar.unimatch.model.skill.SessionType;
import com.rar.unimatch.model.skill.StudyFormat;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Публичная информация о скиле")
public record SkillPublicResponse(
    Long id,
    Boolean isResponse,
    String title,
    String description,
    StudyFormat studyFormat,
    SessionType sessionType,
    RewardType rewardType,
    BigDecimal rewardAmountRub,
    Boolean isActive
) {}
