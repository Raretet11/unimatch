package com.rar.unimatch.model.DTO;

import java.util.List;

public record TagAddRequest(
    List<Long> tagsId
) {}
