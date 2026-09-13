package com.rar.unimatch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.rar.unimatch.model.outbox.OutboxEventType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor 
@Slf4j
public class OutboxPoller {
    private final OutboxService outboxService;

    @Value("${outbox.batch-size}")
    private Long batchSize;

    @Scheduled(fixedDelayString = "${outbox.poll-interval-ms}")
    public void pollEmail() {
        poll(List.of(OutboxEventType.REGISTRATION_EMAIL), "Email");
    }

    @Scheduled(fixedDelayString = "${outbox.poll-interval-ms}")
    public void pollMeilisearch() {
        poll(List.of(OutboxEventType.INDEX_SKILL), "Meilisearch");
    }

    public void poll(List<OutboxEventType> types, String label) {
        try {
            outboxService.executeTasks(batchSize, types);
        } catch (Exception e) {
            log.error("Outbox poller {} failed", label, e);
        }
    }
}
