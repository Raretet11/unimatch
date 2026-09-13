package com.rar.unimatch.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rar.unimatch.error.ResourceNotFoundException;
import com.rar.unimatch.model.outbox.IndexSkillPayload;
import com.rar.unimatch.model.outbox.OutboxEvent;
import com.rar.unimatch.model.outbox.OutboxEventType;
import com.rar.unimatch.model.outbox.SendEmailPayload;
import com.rar.unimatch.model.skill.Skill;
import com.rar.unimatch.model.user.User;
import com.rar.unimatch.repository.OutboxEventsRepository;
import com.rar.unimatch.repository.SkillRepository;
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
    private final MeilisearchService meilisearchService;
    private final SkillRepository skillRepository;

    public void publishSaveSendEmailTask(SendEmailPayload payload) {
        publishTask(OutboxEventType.REGISTRATION_EMAIL, payloadSerializer.toJson(payload));
    }

    public void publishIndexSkillTask(IndexSkillPayload payload) {
        publishTask(OutboxEventType.INDEX_SKILL, payloadSerializer.toJson(payload));
    }

    public void executeTasks(long limit, List<OutboxEventType> type) throws Exception {
        List<OutboxEvent> events = outboxRepository.findBatchToProcess(type.stream().map(Enum::name).toList(), limit);

        for (OutboxEvent event : events) {
            processTask(event);
        }

        if (events.size() > 0) {
            log.info("Outbox finished execution of {} tasks", events.size());
        }
    }

    private void publishTask(OutboxEventType type, String payload) {
        var task = OutboxEvent.builder()
            .eventType(OutboxEventType.INDEX_SKILL)
            .payload(payload)
            .done(false)
            .build();
        outboxRepository.save(task);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void processTask(OutboxEvent task) throws Exception {
        try {
            detectTypeAndHandle(task);
            task.setDone(true);
        } catch (ResourceNotFoundException e) {
            task.setDone(true);
            log.warn("No need to retry error in outbox: ", e);
        } catch (Exception e) {
            task.setAttempts(task.getAttempts() + 1);
            log.warn("Need to retry error in outbox: ", e);
            throw e;
        }

        outboxRepository.save(task);
    }

    private void detectTypeAndHandle(OutboxEvent task) throws Exception {
        switch (task.getEventType()) {
            case OutboxEventType.REGISTRATION_EMAIL:
                handleEmail(task);
                break;

            case OutboxEventType.INDEX_SKILL:
                handleSkill(task);
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

    private void handleSkill(OutboxEvent event) {
        IndexSkillPayload payload = payloadSerializer.fromJson(
            event.getPayload(), IndexSkillPayload.class);

        Skill skill = skillRepository.findById(payload.skillId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Skill not found: " + payload.skillId()));
        
        meilisearchService.indexSkill(skill);
    }
}
