package com.rar.unimatch.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    public void poll() {
        try {
            outboxService.executeTasks(batchSize);
        } catch (Exception e) {
            log.warn("Exception while execute outbox tasks", e);
        }
    }
}
