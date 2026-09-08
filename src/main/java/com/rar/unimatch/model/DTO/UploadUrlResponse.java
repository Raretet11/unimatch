package com.rar.unimatch.model.DTO;

import java.util.Map;

public record UploadUrlResponse (
    String uploadUrl,
    String key,
    Map<String, String> formData
) {}
