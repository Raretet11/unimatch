package com.rar.unimatch.model.DTO;

public record UserLinkCreateRequest(
    String linkName,

    String linkValue,

    Boolean isPublic
) {}
