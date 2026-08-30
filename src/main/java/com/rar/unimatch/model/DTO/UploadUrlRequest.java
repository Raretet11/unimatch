package com.rar.unimatch.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadUrlRequest(
    String fileName,
    String contentType,

    @Schema(description = "-1 для произвольного размера")
    long size
) {}
