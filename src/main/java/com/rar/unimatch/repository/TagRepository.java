package com.rar.unimatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rar.unimatch.model.tag.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query(value = """
        SELECT * FROM tags
        WHERE name % :query
        AND similarity(name, :query) >= :threshold
        ORDER BY similarity(name, :query) DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Tag> findSimilarTags(
        @Param("query") String query,
        @Param("threshold") double threshold,
        @Param("limit") int limit
    );

    Boolean existsByName(String name);
}
