package com.github.wslotkin.itinerator.generator.config;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import java.util.Set;

@Immutable
public interface EvaluationConfig {
    @Default
    default double getCostPenalty() {
        return -10.0;
    }

    @Default
    default double getIncorrectMealPenalty() {
        return -20.0;
    }

    @Default
    default double getAreaHoppingPenalty() {
        return -100.0;
    }

    @Default
    default double getAreaHoppingThreshold() {
        return 20.0;
    }

    @Default
    default double getIncorrectSleepPenalty() {
        return -100.0;
    }

    @Default
    default double getTravelTimePenalty() {
        return -10.0;
    }

    @Default
    default double getInvalidHoursPenalty() {
        return -75.0;
    }

    @Default
    default double getMissingSleepMinutesPenalty() {
        return -5.0;
    }

    @Default
    default double getMissingRequiredEventPenalty() {
        return -100.0;
    }

    Set<String> getRequiredActivities();

    @Default
    default boolean shouldLogEvaluatorResults() {
        return false;
    }
}
