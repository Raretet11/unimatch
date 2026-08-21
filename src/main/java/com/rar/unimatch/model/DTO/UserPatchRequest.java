package com.rar.unimatch.model.DTO;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class UserPatchRequest {
    public Map<String, Object> updates = new HashMap<>();

    @JsonAnySetter
    public void addUpdate(String key, Object value) {
        updates.put(key, value);
    }
}
