package com.rar.unimatch.utils;

import org.springframework.stereotype.Component;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public final class PayloadSerializer {
    private final ObjectMapper objectMapper;

    public String toJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JacksonException e) {
            throw new IllegalStateException(
                "Cannot serialize payload: " + payload.getClass().getName(), e);
        }
    }

    public <T> T fromJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JacksonException e) {
            throw new IllegalArgumentException(
                "Cannot deserialize payload to " + type.getName() + ": " + json, e);
        }
    }
}
