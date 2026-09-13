package com.rar.unimatch.model.outbox;

public record SendEmailPayload(Long userId, String token) {};
