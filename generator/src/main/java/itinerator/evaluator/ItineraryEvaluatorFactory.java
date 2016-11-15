package itinerator.evaluator;

import itinerator.config.EvaluationConfig;
import itinerator.datamodel.Activity;
import itinerator.datamodel.Itinerary;

import java.util.List;

import static itinerator.evaluator.EventEvaluators.eventEvaluators;
import static itinerator.evaluator.RequiredEventsEvaluator.requiredEventsEvaluator;
import static itinerator.evaluator.SubitineraryEvaluators.subitineraryEvaluators;

public class ItineraryEvaluatorFactory {
    public static Evaluator<Itinerary> createEvaluators(EvaluationConfig config, List<Activity> availableActivities) {
        return requiredEventsEvaluator(config, availableActivities)
                .andThen(eventEvaluators(config))
                .andThen(subitineraryEvaluators(config));
    }
}
