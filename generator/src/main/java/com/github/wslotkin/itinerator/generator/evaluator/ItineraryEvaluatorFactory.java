package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.config.EvaluationConfig;
import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;

import java.util.List;

import static com.github.wslotkin.itinerator.generator.evaluator.EventEvaluators.eventEvaluators;
import static com.github.wslotkin.itinerator.generator.evaluator.SubitineraryEvaluators.subitineraryEvaluators;

public class ItineraryEvaluatorFactory {
    public static Evaluator<Itinerary> createEvaluators(EvaluationConfig config, List<Activity> availableActivities) {
        return RequiredEventsEvaluator.requiredEventsEvaluator(config, availableActivities)
                .andThen(eventEvaluators(config))
                .andThen(subitineraryEvaluators(config));
    }
}
