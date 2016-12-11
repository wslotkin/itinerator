package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.datamodel.ActivityType;
import com.github.wslotkin.itinerator.generator.datamodel.Event;
import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;

import java.util.List;
import java.util.function.Predicate;

import static com.github.wslotkin.itinerator.generator.itinerary.TimeUtil.isInSleepWindow;
import static com.google.common.collect.Iterables.getLast;
import static java.lang.Math.abs;

class SleepEventEvaluator implements Evaluator<Itinerary> {
    private final double incorrectSleepPenalty;

    public SleepEventEvaluator(double incorrectSleepPenalty) {
        this.incorrectSleepPenalty = incorrectSleepPenalty;
    }

    @Override
    public double applyAsDouble(Itinerary singleDaySubitinerary) {
        List<Event> events = singleDaySubitinerary.getEvents();
        Event firstEvent = events.get(0);
        Event lastEvent = getLast(events);

        int expectedNumberOfSleepEvents = getNumberOfMatchingEvents(firstEvent, lastEvent, SleepEventEvaluator::eventIsInSleepWindow);
        int numberOfCorrectlyPlacedSleepEvents = getNumberOfMatchingEvents(firstEvent, lastEvent, SleepEventEvaluator::eventIsSleepEvent);
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
        return event.getActivity().getType() == ActivityType.SLEEP;
    }
}
