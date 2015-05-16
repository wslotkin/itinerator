package itinerator.evaluator;

import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import org.joda.time.DateTime;

import java.util.List;

import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newArrayList;
import static itinerator.itinerary.SubitineraryProvider.subitinerary;

public abstract class BaseDaySubitineraryEvaluator implements Evaluator {
    @Override
    public double evaluate(Itinerary itinerary) {
        List<Event> events = itinerary.getEvents();
        if (events.isEmpty()) {
            return 0.0;
        }

        Event firstEvent = events.get(0);
        Event lastEvent = getLast(events);
        DateTime currentStartTime = firstEvent.getEventTime().getStart().withTimeAtStartOfDay();
        DateTime currentEndTime = currentStartTime.plusDays(1);

        List<Itinerary> dailySubitineraries = newArrayList();
        while (currentStartTime.isBefore(lastEvent.getEventTime().getEnd())) {
            Itinerary singleDaySubitinerary = subitinerary(itinerary, currentStartTime, currentEndTime);
            if (!singleDaySubitinerary.getEvents().isEmpty()) {
                dailySubitineraries.add(singleDaySubitinerary);
            }

            currentStartTime = currentEndTime;
            currentEndTime = currentEndTime.plusDays(1);
        }

        return dailySubitineraries.stream()
                .mapToDouble(this::evaluateDaySubitinerary)
                .sum();
    }

    protected abstract double evaluateDaySubitinerary(Itinerary singleDaySubitinerary);
}
