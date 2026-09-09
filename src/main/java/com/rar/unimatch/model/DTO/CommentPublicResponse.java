package com.rar.unimatch.model.DTO;

public record CommentPublicResponse(
    Long id,
    Long parentCommentId,
    Long fromUserId,
    Long toUserId,
    Integer rating,
    String commentText
) {}
