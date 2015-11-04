package itinerator.evaluator;

import com.google.common.annotations.VisibleForTesting;
import itinerator.config.EvaluationConfig;
import itinerator.datamodel.Itinerary;

import static itinerator.evaluator.Evaluator.compose;

public class ItineraryEvaluators implements Evaluator<Itinerary> {

    private final Evaluator<Itinerary> evaluator;
    private final DaySubitineraryProvider subitineraryProvider;

    public static Evaluator<Itinerary> createEvaluators(EvaluationConfig config) {
        return new ItineraryEvaluators(new DaySubitineraryProvider(),
                EventEvaluators.createEvaluators(config),
                new SleepEventEvaluator(config.getIncorrectSleepPenalty()),
                new MealEvaluator(config.getIncorrectMealPenalty()));
    }

    @VisibleForTesting
    @SafeVarargs
    ItineraryEvaluators(DaySubitineraryProvider subitineraryProvider,
                        Evaluator<Itinerary> first,
                        Evaluator<Itinerary>... rest) {
        this.subitineraryProvider = subitineraryProvider;
        this.evaluator = compose(first, rest);
    }

    @Override
    public double applyAsDouble(Itinerary itinerary) {
        return subitineraryProvider.getPerDaySubitineraries(itinerary).stream()
                .mapToDouble(evaluator::applyAsDouble)
                .sum();
    }
}
