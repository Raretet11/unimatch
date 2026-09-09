package com.rar.unimatch.model.mapper;

import org.springframework.stereotype.Component;

import com.rar.unimatch.model.DTO.CommentPublicResponse;
import com.rar.unimatch.model.comment.Comment;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CommentMapper {
    public CommentPublicResponse toPublicResponse(Comment comment) {
        if (comment == null) {
            return null;
        }

        return new CommentPublicResponse(
            comment.getId(), 
            comment.getParentComment().getId(), 
            comment.getFromUser().getId(),
            comment.getToUser().getId(), 
            comment.getRating(), 
            comment.getCommentText()
        );
    }
}
