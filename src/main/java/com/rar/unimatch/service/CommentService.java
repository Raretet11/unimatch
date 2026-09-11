package com.rar.unimatch.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rar.unimatch.error.BadRequestException;
import com.rar.unimatch.model.DTO.CommentCreateRequest;
import com.rar.unimatch.model.comment.Comment;
import com.rar.unimatch.model.comment.RatingSummary;
import com.rar.unimatch.model.user.User;
import com.rar.unimatch.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository repository;

    public Comment save(CommentCreateRequest request, User user) {
        if (user.getId().equals(request.toUserId())) {
            throw new BadRequestException("Can't comment on yourself");
        }

        if (request.parentCommentId() != null) {
            Comment parentComment = repository.getReferenceById(request.parentCommentId());
            if (!parentComment.getToUserId().equals(request.toUserId())) {
                throw new BadRequestException("To user id has to be the same as parent's comment to user id");
            }
        }

        Comment comment = Comment.builder()
            .commentText(request.commentText())
            .parentCommentId(request.parentCommentId())
            .fromUserId(user.getId())
            .toUserId(request.toUserId())
            .rating(request.rating())
            .build();

        log.info("Create comment from user {} to user {}", user.getId(), request.toUserId());
        return repository.save(comment);
    }

    public void delete(long id) {
        log.info("Delete comment with id: {}", id);
        repository.deleteById(id);
    }

    public RatingSummary getRatingSummary(long id) {
        return repository.getRatingSummary(id);
    }

    public List<Comment> getByUser(long userId) {
        return repository.findByToUserId(userId);
    }
}
