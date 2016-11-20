package itinerator.evaluator;

import com.google.common.annotations.VisibleForTesting;
import itinerator.config.EvaluationConfig;
import itinerator.datamodel.Itinerary;

public class SubitineraryEvaluators implements Evaluator<Itinerary> {

    private final Evaluator<Itinerary> subitineraryEvaluator;
    private final DaySubitineraryProvider subitineraryProvider;

    public static Evaluator<Itinerary> subitineraryEvaluators(EvaluationConfig config) {
        return new SubitineraryEvaluators(new DaySubitineraryProvider(),
                new SleepEventEvaluator(config.getIncorrectSleepPenalty())
                        .andThen(new MealEvaluator(config.getIncorrectMealPenalty())));
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
