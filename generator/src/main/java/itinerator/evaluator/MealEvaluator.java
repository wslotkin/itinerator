package itinerator.evaluator;

import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import itinerator.datamodel.Range;

import java.util.List;

import static com.google.common.collect.Iterables.getLast;
import static itinerator.datamodel.ActivityType.FOOD;
import static itinerator.itinerary.TimeUtil.numberOfMealsInTimeRange;
import static java.lang.Math.abs;

class MealEvaluator implements Evaluator<Itinerary> {
    private final double incorrectMealPenalty;

    public MealEvaluator(double incorrectMealPenalty) {
        this.incorrectMealPenalty = incorrectMealPenalty;
    }

    @Override
    public double applyAsDouble(Itinerary singleDaySubitinerary) {
        List<Event> events = singleDaySubitinerary.getEvents();
        Event firstEvent = events.get(0);
        Event lastEvent = getLast(events);

        int expectedNumberOfMeals = numberOfMealsInTimeRange(Range.of(firstEvent.getEventTime().getStart(),
                lastEvent.getEventTime().getEnd()));
        int actualNumberOfMeals = (int) events.stream()
                .filter(MealEvaluator::eventIsMealEvent)
                .count();

        return incorrectMealPenalty * abs(expectedNumberOfMeals - actualNumberOfMeals);
    }

    private static boolean eventIsMealEvent(Event event) {
        return event.getActivity().getType() == FOOD;
    }
}
