package com.rar.unimatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rar.unimatch.model.comment.Comment;
import com.rar.unimatch.model.comment.RatingSummary;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = """
        SELECT COALESCE(AVG(rating), 0) AS avg,
            COUNT(*) AS total
        FROM comments
        WHERE to_user_id = :userId
        """, nativeQuery = true)
    RatingSummary getRatingSummary(@Param("userId") Long userId);
}
