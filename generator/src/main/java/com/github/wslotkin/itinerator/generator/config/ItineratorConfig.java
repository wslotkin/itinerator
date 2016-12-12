package com.github.wslotkin.itinerator.generator.config;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Immutable
public interface ItineratorConfig {
    @Default
    default LocalDateTime getStartTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0));
    }

    @Default
    default LocalDateTime getEndTime() {
        return getStartTime().plusDays(2);
    }

    Set<String> getExcludedActivityIds();
}
