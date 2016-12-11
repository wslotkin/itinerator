package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.datamodel.Event;
import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.github.wslotkin.itinerator.generator.itinerary.SubitineraryProvider.subitinerary;
import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;

class DaySubitineraryProvider {

    public List<Itinerary> getPerDaySubitineraries(Itinerary itinerary) {
        List<Event> events = itinerary.getEvents();
        if (events.isEmpty()) {
            return emptyList();
        }

        Event firstEvent = events.get(0);
        Event lastEvent = getLast(events);
        LocalDateTime currentStartTime = LocalDateTime.of(firstEvent.getEventTime().getStart().toLocalDate(), LocalTime.of(0, 0));
        LocalDateTime currentEndTime = currentStartTime.plusDays(1);

        List<Itinerary> dailySubitineraries = newArrayList();
        while (currentStartTime.isBefore(lastEvent.getEventTime().getEnd())) {
            Itinerary singleDaySubitinerary = subitinerary(itinerary, currentStartTime, currentEndTime);
            if (!singleDaySubitinerary.getEvents().isEmpty()) {
                dailySubitineraries.add(singleDaySubitinerary);
            }

            currentStartTime = currentEndTime;
            currentEndTime = currentEndTime.plusDays(1);
        }
        return dailySubitineraries;
    }
}
