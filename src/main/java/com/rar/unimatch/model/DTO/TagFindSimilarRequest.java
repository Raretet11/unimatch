package com.rar.unimatch.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record TagFindSimilarRequest(
    String tag,

    @Schema(description = "Определяет уровень похожести, от 0 до 1, все что ниже трешхолда не возвращается")
    Double threshold,

    @Schema(description = "Количество вернувшихся записей")
    Integer limit
) {}
