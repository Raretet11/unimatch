package com.rar.unimatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rar.unimatch.model.outbox.OutboxEvent;

public interface OutboxEventsRepository extends JpaRepository<OutboxEvent, Long> {
    @Query(value = """
        SELECT * FROM outbox_events
        WHERE done = false
        ORDER BY created_at
        LIMIT :limit
        """, nativeQuery = true)
    List<OutboxEvent> findBatchToProcess(@Param("limit") long limit);
}
