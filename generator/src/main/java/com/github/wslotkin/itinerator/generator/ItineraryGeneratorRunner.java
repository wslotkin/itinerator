package com.github.wslotkin.itinerator.generator;

import com.github.wslotkin.itinerator.generator.config.ItineratorConfig;
import com.github.wslotkin.itinerator.generator.config.OptimizationConfig;
import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.datamodel.SolverResult;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ItineraryGeneratorRunner {
    private final ItineratorConfig itineratorConfig;
    private final ActivityProvider activityProvider;
    private final ItineraryGenerator itineraryGenerator;

    public static ItineraryGeneratorRunner createOptimizer(OptimizationConfig optimizationConfig, ActivityProvider activityProvider) {
        return new ItineraryGeneratorRunner(optimizationConfig.getItineratorConfig(),
                activityProvider,
                new ItineraryOptimizer(optimizationConfig));
    }

    public ItineraryGeneratorRunner(ItineratorConfig itineratorConfig,
                                    ActivityProvider activityProvider,
                                    ItineraryGenerator itineraryGenerator) {
        this.itineratorConfig = itineratorConfig;
        this.activityProvider = activityProvider;
        this.itineraryGenerator = itineraryGenerator;
    }

    public SolverResult run() {
        List<Activity> activities = activityProvider.getActivities();

        List<Activity> filteredActivities = activities.stream()
                .filter(activity -> !itineratorConfig.getExcludedActivityIds().contains(activity.getId()))
                .collect(toList());

        return itineraryGenerator.getResult(filteredActivities,
                activityProvider.getFixedEvents(),
                itineratorConfig.getStartTime(),
                itineratorConfig.getEndTime());
    }
}
