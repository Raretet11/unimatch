package com.rar.unimatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rar.unimatch.model.comment.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
