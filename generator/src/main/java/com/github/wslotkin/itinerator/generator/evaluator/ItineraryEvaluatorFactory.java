package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.config.EvaluationConfig;
import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;

import java.util.List;

import static com.github.wslotkin.itinerator.generator.evaluator.EvaluatorFactory.evaluator;
import static com.github.wslotkin.itinerator.generator.evaluator.EvaluatorType.REQUIRED;
import static com.github.wslotkin.itinerator.generator.evaluator.EvaluatorType.TOTAL;
import static com.github.wslotkin.itinerator.generator.evaluator.EventEvaluators.eventEvaluators;
import static com.github.wslotkin.itinerator.generator.evaluator.RequiredEventsEvaluator.requiredEventsEvaluator;
import static com.github.wslotkin.itinerator.generator.evaluator.SubitineraryEvaluators.subitineraryEvaluators;

public class ItineraryEvaluatorFactory {
    public static Evaluator<Itinerary> createEvaluators(EvaluationConfig config, List<Activity> availableActivities) {
        return createEvaluators(config, availableActivities, null);
    }

    public static Evaluator<Itinerary> createEvaluators(EvaluationConfig config, List<Activity> availableActivities, EvaluatorState evaluatorState) {
        Evaluator<Itinerary> compositeEvaluator = evaluator(requiredEventsEvaluator(config, availableActivities), evaluatorState, REQUIRED)
                .andThen(eventEvaluators(config, evaluatorState))
                .andThen(subitineraryEvaluators(config, evaluatorState));
        return evaluator(compositeEvaluator, evaluatorState, TOTAL);
    }
}
