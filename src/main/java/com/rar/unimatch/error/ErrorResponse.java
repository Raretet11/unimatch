package com.rar.unimatch.error;

import java.time.Instant;
import java.time.ZonedDateTime;

import com.rar.unimatch.utils.ClockProvider;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private ZonedDateTime timestamp;
    private int status;
    private String error;
    private String message;

    public ErrorResponse(int status, String error, String message) {
        this.timestamp = Instant.now().atZone(ClockProvider.MOSCOW_ZONE);
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
