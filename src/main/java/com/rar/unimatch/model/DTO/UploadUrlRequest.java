package com.rar.unimatch.model.DTO;

public record UploadUrlRequest(
    String fileName,
    String contentType,
    long size
) {}
