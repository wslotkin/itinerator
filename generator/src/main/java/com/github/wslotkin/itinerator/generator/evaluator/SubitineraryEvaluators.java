package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.config.EvaluationConfig;
import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;
import com.google.common.annotations.VisibleForTesting;

import static com.github.wslotkin.itinerator.generator.evaluator.EvaluatorFactory.evaluator;
import static com.github.wslotkin.itinerator.generator.evaluator.EvaluatorType.MEAL_EVENT;
import static com.github.wslotkin.itinerator.generator.evaluator.EvaluatorType.SLEEP_EVENT;

class SubitineraryEvaluators implements Evaluator<Itinerary> {

    private final Evaluator<Itinerary> subitineraryEvaluator;
    private final DaySubitineraryProvider subitineraryProvider;

    public static Evaluator<Itinerary> subitineraryEvaluators(EvaluationConfig config, EvaluatorState evaluatorState) {
        return new SubitineraryEvaluators(new DaySubitineraryProvider(),
                evaluator(new SleepEventEvaluator(config.getIncorrectSleepPenalty()), evaluatorState, SLEEP_EVENT)
                        .andThen(evaluator(new MealEvaluator(config.getIncorrectMealPenalty()), evaluatorState, MEAL_EVENT)));
    }

    @VisibleForTesting
    SubitineraryEvaluators(DaySubitineraryProvider subitineraryProvider,
                           Evaluator<Itinerary> subitineraryEvaluator) {
        this.subitineraryProvider = subitineraryProvider;
        this.subitineraryEvaluator = subitineraryEvaluator;
    }

    @Override
    public double applyAsDouble(Itinerary itinerary) {
        return subitineraryProvider.getPerDaySubitineraries(itinerary).stream()
                .mapToDouble(subitineraryEvaluator)
                .sum();
    }
}
