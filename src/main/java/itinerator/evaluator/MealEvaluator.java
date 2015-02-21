package itinerator.evaluator;

import com.google.common.annotations.VisibleForTesting;
import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;

import java.util.List;

import static com.google.common.collect.Iterables.getLast;
import static itinerator.datamodel.ActivityType.FOOD;
import static itinerator.itinerary.TimeUtil.numberOfMealsInTimeRange;
import static java.lang.Math.abs;

public class MealEvaluator extends BaseDaySubitineraryEvaluator {
    @VisibleForTesting
    static final double INCORRECT_MEAL_PENALTY = -20.0;

    @Override
    protected double evaluateDaySubitinerary(Itinerary singleDaySubitinerary) {
        List<Event> events = singleDaySubitinerary.getEvents();
        Event firstEvent = events.get(0);
        Event lastEvent = getLast(events);

        int expectedNumberOfMeals = numberOfMealsInTimeRange(firstEvent.getEventTime().getStart(),
                lastEvent.getEventTime().getEnd());
        int actualNumberOfMeals = events.stream()
                .mapToInt(a -> eventIsMealEvent(a) ? 1 : 0)
                .sum();

        return INCORRECT_MEAL_PENALTY * abs(expectedNumberOfMeals - actualNumberOfMeals);
    }

    private static boolean eventIsMealEvent(Event event) {
        return event.getActivity().getType() == FOOD;
    }
}
