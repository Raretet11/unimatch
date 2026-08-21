package com.rar.unimatch.model.DTO;

import java.math.BigDecimal;

import com.rar.unimatch.model.skill.RewardType;
import com.rar.unimatch.model.skill.SessionType;
import com.rar.unimatch.model.skill.StudyFormat;

public record SkillCreateRequest (
    Boolean isResponse,
    String title,
    String description,
    StudyFormat studyFormat,
    SessionType sessionType,
    RewardType rewardType,
    BigDecimal rewardAmountRub
) {}
