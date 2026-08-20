package com.rar.unimatch.utils;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockProvider {
    public static final ZoneId MOSCOW_ZONE = ZoneId.of("Europe/Moscow");

    public static Clock clock() {
        return Clock.system(MOSCOW_ZONE);
    }
}
