package com.github.wslotkin.itinerator.generator.datamodel;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import java.time.LocalDateTime;

@Immutable
public interface Event {
    @Parameter
    @Default
    default Activity getActivity() {
        return ImmutableActivity.builder().build();
    }

    @Parameter
    @Default
    default Range<LocalDateTime> getEventTime() {
        return Range.of(LocalDateTime.MIN, LocalDateTime.MIN);
    }

    @Parameter
    @Default
    default double getTravelTime() {
        return 0.0;
    }
}
