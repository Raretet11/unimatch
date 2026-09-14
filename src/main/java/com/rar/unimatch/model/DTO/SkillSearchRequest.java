package com.rar.unimatch.model.DTO;

import java.math.BigDecimal;
import java.util.List;

public record SkillSearchRequest(
    String query,
    List<String> genders,
    List<String> degree,
    List<Integer> courses,
    List<String> campus,
    List<String> sessionTypes,
    Boolean isActive,
    List<String> rewardTypes,
    BigDecimal minReward,
    BigDecimal maxReward,
    String sortBy,
    String sortOrder,
    int limit,
    int offset
) {}
