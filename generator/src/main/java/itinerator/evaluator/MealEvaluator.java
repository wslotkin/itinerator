package itinerator.evaluator;

import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;

import java.util.List;

import static com.google.common.collect.Iterables.getLast;
import static itinerator.datamodel.ActivityType.FOOD;
import static itinerator.itinerary.TimeUtil.numberOfMealsInTimeRange;
import static java.lang.Math.abs;

public class MealEvaluator extends BaseDaySubitineraryEvaluator {
    private final double incorrectMealPenalty;

    public MealEvaluator(double incorrectMealPenalty) {
        this.incorrectMealPenalty = incorrectMealPenalty;
    }

    @Override
    protected double evaluateDaySubitinerary(Itinerary singleDaySubitinerary) {
        List<Event> events = singleDaySubitinerary.getEvents();
        Event firstEvent = events.get(0);
        Event lastEvent = getLast(events);

        int expectedNumberOfMeals = numberOfMealsInTimeRange(firstEvent.getEventTime().getStartMillis(),
                lastEvent.getEventTime().getEndMillis());
        int actualNumberOfMeals = events.stream()
                .mapToInt(event -> eventIsMealEvent(event) ? 1 : 0)
                .sum();

        return incorrectMealPenalty * abs(expectedNumberOfMeals - actualNumberOfMeals);
    }

    private static boolean eventIsMealEvent(Event event) {
        return event.getActivity().getType() == FOOD;
    }
}
