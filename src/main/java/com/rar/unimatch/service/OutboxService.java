package com.rar.unimatch.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rar.unimatch.error.ResourceNotFoundException;
import com.rar.unimatch.model.outbox.OutboxEvent;
import com.rar.unimatch.model.outbox.OutboxEventType;
import com.rar.unimatch.model.outbox.SendEmailPayload;
import com.rar.unimatch.model.user.User;
import com.rar.unimatch.repository.OutboxEventsRepository;
import com.rar.unimatch.repository.UserRepository;
import com.rar.unimatch.utils.PayloadSerializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxService {
    private final OutboxEventsRepository outboxRepository;
    private final PayloadSerializer payloadSerializer;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public void publishSaveSendEmailTask(SendEmailPayload payload) {
        var outboxEvent = OutboxEvent.builder()
            .eventType(OutboxEventType.REGISTRATION_EMAIL)
            .payload(payloadSerializer.toJson(payload))
            .done(false)
            .build();
        publishEvent(outboxEvent);
    }

    public void executeTasks(long limit) throws Exception {
        List<OutboxEvent> events = outboxRepository.findBatchToProcess(limit);

        for (OutboxEvent event : events) {
            processEvent(event);
        }

        if (events.size() > 0) {
            log.info("Outbox finished execution of {} tasks", events.size());
        }
    }

    private void publishEvent(OutboxEvent outboxEvent) {
        outboxRepository.save(outboxEvent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void processEvent(OutboxEvent event) throws Exception {
        try {
            detectTypeAndHandle(event);
            event.setDone(true);
        } catch (ResourceNotFoundException e) {
            event.setDone(true);
            log.warn("No need to retry error in outbox: ", e);
        } catch (Exception e) {
            event.setAttempts(event.getAttempts() + 1);
            log.warn("Need to retry error in outbox: ", e);
            throw e;
        }

        outboxRepository.save(event);
    }

    private void detectTypeAndHandle(OutboxEvent event) throws Exception {
        switch (event.getEventType()) {
            case OutboxEventType.REGISTRATION_EMAIL:
                handleEmail(event);
                break;

            default:
                break;
        }
    }

    private void handleEmail(OutboxEvent event) throws Exception {
        SendEmailPayload payload = payloadSerializer.fromJson(
            event.getPayload(), SendEmailPayload.class);

        User user = userRepository.findById(payload.userId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found: " + payload.userId()));

        emailService.sendVerificationEmail(user, payload.token());
    }
}
