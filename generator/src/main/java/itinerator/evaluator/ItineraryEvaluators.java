package itinerator.evaluator;

import com.google.common.annotations.VisibleForTesting;
import itinerator.config.EvaluationConfig;
import itinerator.datamodel.Itinerary;

public class ItineraryEvaluators implements Evaluator<Itinerary> {

    private final Evaluator<Itinerary> subitineraryEvaluator;
    private final DaySubitineraryProvider subitineraryProvider;

    public static Evaluator<Itinerary> createEvaluators(EvaluationConfig config) {
        return new ItineraryEvaluators(new DaySubitineraryProvider(),
                EventEvaluators.createEvaluators(config)
                        .andThen(new SleepEventEvaluator(config.getIncorrectSleepPenalty()))
                        .andThen(new MealEvaluator(config.getIncorrectMealPenalty())));
    }

    @VisibleForTesting
    ItineraryEvaluators(DaySubitineraryProvider subitineraryProvider,
                        Evaluator<Itinerary> subitineraryEvaluator) {
        this.subitineraryProvider = subitineraryProvider;
        this.subitineraryEvaluator = subitineraryEvaluator;
    }

    @Override
    public double applyAsDouble(Itinerary itinerary) {
        return subitineraryProvider.getPerDaySubitineraries(itinerary).stream()
                .mapToDouble(subitineraryEvaluator::applyAsDouble)
                .sum();
    }
}
