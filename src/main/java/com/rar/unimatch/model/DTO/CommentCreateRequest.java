package com.rar.unimatch.model.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
    Long toUserId,

    @Min(value = 1)
    @Max(value = 5)
    Integer rating,

    @Size(max = 1000)
    String commentText,

    Long parentCommentId
) {}
