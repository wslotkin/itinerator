package itinerator.evaluator;

import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;

import java.util.List;
import java.util.function.Predicate;

import static com.google.common.collect.Iterables.getLast;
import static itinerator.datamodel.ActivityType.SLEEP;
import static itinerator.itinerary.TimeUtil.isInSleepWindow;
import static java.lang.Math.abs;

public class SleepEvaluator extends BaseDaySubitineraryEvaluator {
    private final double incorrectSleepPenalty;

    public SleepEvaluator(double incorrectSleepPenalty) {
        this.incorrectSleepPenalty = incorrectSleepPenalty;
    }

    @Override
    protected double evaluateDaySubitinerary(Itinerary singleDaySubitinerary) {
        List<Event> events = singleDaySubitinerary.getEvents();
        Event firstEvent = events.get(0);
        Event lastEvent = getLast(events);

        int expectedNumberOfSleepEvents = getNumberOfMatchingEvents(firstEvent, lastEvent, SleepEvaluator::eventIsInSleepWindow);
        int numberOfCorrectlyPlacedSleepEvents = getNumberOfMatchingEvents(firstEvent, lastEvent, SleepEvaluator::eventIsSleepEvent);
        int totalNumberOfSleepEvents = events.stream()
                .mapToInt(event -> eventIsSleepEvent(event) ? 1 : 0)
                .sum();

        int numberOfUnexpectedSleepEvents = abs(totalNumberOfSleepEvents - numberOfCorrectlyPlacedSleepEvents);
        int differenceBetweenExpectedAndActual = abs(expectedNumberOfSleepEvents - numberOfCorrectlyPlacedSleepEvents);

        return incorrectSleepPenalty * (numberOfUnexpectedSleepEvents + differenceBetweenExpectedAndActual);
    }

    private static int getNumberOfMatchingEvents(Event firstEvent, Event lastEvent, Predicate<Event> eventPredicate) {
        if (eventPredicate.test(firstEvent) && eventPredicate.test(lastEvent)) {
            return 2;
        } else if (eventPredicate.test(firstEvent) || eventPredicate.test(lastEvent)) {
            return 1;
        } else {
            return 0;
        }
    }

    private static boolean eventIsInSleepWindow(Event event) {
        return isInSleepWindow(event.getEventTime().getStart());
    }

    private static boolean eventIsSleepEvent(Event event) {
        return event.getActivity().getType() == SLEEP;
    }
}
