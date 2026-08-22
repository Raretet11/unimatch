package com.rar.unimatch.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Публичная информация о теге")
public record TagPublicResponse(
    Long id,
    String name,
    Integer usageCount
) {}
