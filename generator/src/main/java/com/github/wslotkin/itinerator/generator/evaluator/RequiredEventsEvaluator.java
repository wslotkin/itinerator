package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.config.EvaluationConfig;
import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.datamodel.Event;
import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;

import static java.lang.Math.abs;
import static java.util.stream.Collectors.toSet;

class RequiredEventsEvaluator implements Evaluator<Itinerary> {

    private final Set<Activity> requiredActivities;
    private final double perEventPenalty;

    public static Evaluator<Itinerary> requiredEventsEvaluator(EvaluationConfig config, List<Activity> availableActivities) {
        Set<Activity> requiredActivities = availableActivities.stream()
                .filter(activity -> config.getRequiredActivities().contains(activity.getId()))
                .collect(toSet());
        return !requiredActivities.isEmpty()
                ? new RequiredEventsEvaluator(requiredActivities, config.getMissingRequiredEventPenalty())
                : itinerary -> 0.0;
    }

    @VisibleForTesting
    RequiredEventsEvaluator(Set<Activity> requiredActivities, double perEventPenalty) {
        this.perEventPenalty = perEventPenalty;
        this.requiredActivities = ImmutableSet.copyOf(requiredActivities);
    }

    @Override
    public double applyAsDouble(Itinerary itinerary) {
        int includedRequiredActivities = 0;
        for (Event event : itinerary.getEvents()) {
            Activity activity = event.getActivity();
            if (requiredActivities.contains(activity)) {
                includedRequiredActivities++;
            }
        }
        return abs(includedRequiredActivities - requiredActivities.size()) * perEventPenalty;
    }
}
