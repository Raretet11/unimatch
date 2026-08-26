package com.rar.unimatch.model.DTO;

public record UserLinkPublicResponse(
    Long id,

    String linkName,

    String linkValue,

    Boolean isPublic
) {}
