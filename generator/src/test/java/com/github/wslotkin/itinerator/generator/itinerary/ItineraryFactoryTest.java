package com.github.wslotkin.itinerator.generator.itinerary;

import com.github.wslotkin.itinerator.generator.calculators.RoundingTravelTimeCalculator;
import com.github.wslotkin.itinerator.generator.datamodel.*;
import cz.cvut.felk.cig.jcop.problem.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.github.wslotkin.itinerator.generator.datamodel.ActivityType.*;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ItineraryFactoryTest {

    private static final Configuration CONFIGURATION = new Configuration(newArrayList(3, 4, 2, 66, 44, 33, 65, 14, 35, 46, 36, 25, 333));
    private static final List<Activity> ACTIVITIES = createActivities();
    private static final LocalDateTime START_TIME = LocalDateTime.of(2015, 2, 21, 21, 0);
    private static final LocalDateTime END_TIME = START_TIME.plusDays(2);

    private RoundingTravelTimeCalculator travelTimeCalculator;
    private ItineraryFactory itineraryFactory;

    @Before
    public void before() {
        travelTimeCalculator = mock(RoundingTravelTimeCalculator.class);

        itineraryFactory = new ItineraryFactory(ACTIVITIES, START_TIME, END_TIME, travelTimeCalculator, new ArrayList<>());
    }

    @Test
    public void createsExpectedItinerary() {
        Itinerary itinerary = itineraryFactory.create(CONFIGURATION);

        assertTrue(itinerary.getEvents().containsAll(expectedEvents()));
        assertEquals(2, numberOfEventsMatchingType(itinerary, SLEEP));
        assertEquals(6, numberOfEventsMatchingType(itinerary, FOOD));
        assertEquals(12, numberOfEventsMatchingType(itinerary, ACTIVITY));
    }

    @Test
    public void correctlyAddsFixedEventsIntoExpectedItinerary() {
        Event fixedEvent = ImmutableEvent.builder()
                .activity(ImmutableActivity.builder()
                        .id("fixed event")
                        .type(ActivityType.ACTIVITY)
                        .duration(0L)
                        .build())
                .eventTime(Range.of(LocalDateTime.of(2015, 2, 22, 16, 0), LocalDateTime.of(2015, 2, 22, 16, 0)))
                .build();
        itineraryFactory = new ItineraryFactory(ACTIVITIES, START_TIME, END_TIME, travelTimeCalculator, newArrayList(fixedEvent));

        Itinerary itinerary = itineraryFactory.create(CONFIGURATION);

        assertTrue(itinerary.getEvents().containsAll(expectedEvents()));
        assertEquals(2, numberOfEventsMatchingType(itinerary, SLEEP));
        assertEquals(6, numberOfEventsMatchingType(itinerary, FOOD));
        assertEquals(13, numberOfEventsMatchingType(itinerary, ACTIVITY));
        assertTrue(itinerary.getEvents().contains(fixedEvent));
    }

    @Test
    public void handlesStartTimeInSleepWindow() {
        itineraryFactory = new ItineraryFactory(ACTIVITIES, LocalDateTime.of(2015, 2, 21, 23, 0), END_TIME, travelTimeCalculator, new ArrayList<>());

        Itinerary itinerary = itineraryFactory.create(CONFIGURATION);

        assertEquals(2, numberOfEventsMatchingType(itinerary, SLEEP));
        assertEquals(6, numberOfEventsMatchingType(itinerary, FOOD));
        assertEquals(11, numberOfEventsMatchingType(itinerary, ACTIVITY));
    }

    @Test
    public void handlesStartTimeInMealWindow() {
        itineraryFactory = new ItineraryFactory(ACTIVITIES, LocalDateTime.of(2015, 2, 21, 19, 0), END_TIME, travelTimeCalculator, new ArrayList<>());

        Itinerary itinerary = itineraryFactory.create(CONFIGURATION);

        assertEquals(2, numberOfEventsMatchingType(itinerary, SLEEP));
        assertEquals(7, numberOfEventsMatchingType(itinerary, FOOD));
        assertEquals(12, numberOfEventsMatchingType(itinerary, ACTIVITY));
    }

    private static long numberOfEventsMatchingType(Itinerary itinerary, ActivityType activityType) {
        return itinerary.getEvents().stream()
                .filter(a -> a.getActivity().getType() == activityType)
                .count();
    }

    private static List<Activity> createActivities() {
        return newArrayList(ImmutableActivity.builder().id("1").duration(120L).build(),
                ImmutableActivity.builder().id("2").duration(120L).build(),
                ImmutableActivity.builder().id("3").duration(120L).build(),
                ImmutableActivity.builder().id("4").duration(120L).build(),
                ImmutableActivity.builder().id("5").duration(120L).build(),
                ImmutableActivity.builder().id("6").duration(120L).build(),
                ImmutableActivity.builder().id("7").duration(120L).build(),
                ImmutableActivity.builder().id("8").duration(120L).build(),
                ImmutableActivity.builder().id("9").duration(120L).build(),
                ImmutableActivity.builder().id("10").duration(120L).build(),
                ImmutableActivity.builder().id("11").duration(120L).build(),
                ImmutableActivity.builder().id("12").duration(120L).build(),
                ImmutableActivity.builder().id("13").duration(120L).build());
    }

    private static List<Event> expectedEvents() {
        return newArrayList(ImmutableEvent.builder().activity(ACTIVITIES.get(2)).eventTime(createEventInterval(START_TIME)).build(),
                ImmutableEvent.builder().activity(ACTIVITIES.get(0)).eventTime(createEventInterval(LocalDateTime.of(2015, 2, 22, 9, 0))).build(),
                ImmutableEvent.builder().activity(ACTIVITIES.get(1)).eventTime(createEventInterval(LocalDateTime.of(2015, 2, 22, 12, 0))).build(),
                ImmutableEvent.builder().activity(ACTIVITIES.get(7)).eventTime(createEventInterval(LocalDateTime.of(2015, 2, 22, 14, 0))).build(),
                ImmutableEvent.builder().activity(ACTIVITIES.get(11)).eventTime(createEventInterval(LocalDateTime.of(2015, 2, 22, 16, 0))).build(),
                ImmutableEvent.builder().activity(ACTIVITIES.get(5)).eventTime(createEventInterval(LocalDateTime.of(2015, 2, 22, 19, 0))).build(),
                ImmutableEvent.builder().activity(ACTIVITIES.get(8)).eventTime(createEventInterval(LocalDateTime.of(2015, 2, 22, 21, 0))).build(),
                ImmutableEvent.builder().activity(ACTIVITIES.get(10)).eventTime(createEventInterval(LocalDateTime.of(2015, 2, 23, 9, 0))).build(),
                ImmutableEvent.builder().activity(ACTIVITIES.get(4)).eventTime(createEventInterval(LocalDateTime.of(2015, 2, 23, 12, 0))).build(),
                ImmutableEvent.builder().activity(ACTIVITIES.get(9)).eventTime(createEventInterval(LocalDateTime.of(2015, 2, 23, 14, 0))).build(),
                ImmutableEvent.builder().activity(ACTIVITIES.get(6)).eventTime(createEventInterval(LocalDateTime.of(2015, 2, 23, 16, 0))).build());
    }

    private static Range<LocalDateTime> createEventInterval(LocalDateTime startTime) {
        return Range.of(startTime, startTime.plusHours(2));
    }
}