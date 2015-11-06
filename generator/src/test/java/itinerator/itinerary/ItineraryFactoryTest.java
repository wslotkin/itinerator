package itinerator.itinerary;

import cz.cvut.felk.cig.jcop.problem.Configuration;
import itinerator.calculators.RoundingTravelTimeCalculator;
import itinerator.datamodel.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static itinerator.datamodel.ActivityType.*;
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
        Event fixedEvent = new TestEventBuilder()
                .setActivity(new ActivityBuilder()
                        .setId("fixed event")
                        .setType(ActivityType.ACTIVITY)
                        .setDuration(0L)
                        .build())
                .setEventTime(Range.of(LocalDateTime.of(2015, 2, 22, 16, 0), LocalDateTime.of(2015, 2, 22, 16, 0)))
                .build();
        itineraryFactory = new ItineraryFactory(ACTIVITIES, START_TIME, END_TIME, travelTimeCalculator, newArrayList(fixedEvent));

        Itinerary itinerary = itineraryFactory.create(CONFIGURATION);

        assertTrue(itinerary.getEvents().containsAll(expectedEvents()));
        assertEquals(2, numberOfEventsMatchingType(itinerary, SLEEP));
        assertEquals(6, numberOfEventsMatchingType(itinerary, FOOD));
        assertEquals(13, numberOfEventsMatchingType(itinerary, ACTIVITY));
        assertTrue(itinerary.getEvents().contains(fixedEvent));
    }

    private static long numberOfEventsMatchingType(Itinerary itinerary, ActivityType activityType) {
        return itinerary.getEvents().stream()
                .filter(a -> a.getActivity().getType() == activityType)
                .count();
    }

    private static List<Activity> createActivities() {
        return newArrayList(new ActivityBuilder().setId("1").setDuration(120L).build(),
                new ActivityBuilder().setId("2").setDuration(120L).build(),
                new ActivityBuilder().setId("3").setDuration(120L).build(),
                new ActivityBuilder().setId("4").setDuration(120L).build(),
                new ActivityBuilder().setId("5").setDuration(120L).build(),
                new ActivityBuilder().setId("6").setDuration(120L).build(),
                new ActivityBuilder().setId("7").setDuration(120L).build(),
                new ActivityBuilder().setId("8").setDuration(120L).build(),
                new ActivityBuilder().setId("9").setDuration(120L).build(),
                new ActivityBuilder().setId("10").setDuration(120L).build(),
                new ActivityBuilder().setId("11").setDuration(120L).build(),
                new ActivityBuilder().setId("12").setDuration(120L).build(),
                new ActivityBuilder().setId("13").setDuration(120L).build());
    }

    private static List<Event> expectedEvents() {
        return newArrayList(new TestEventBuilder().setActivity(ACTIVITIES.get(2)).setEventTime(createEventInterval(START_TIME)).build(),
                new TestEventBuilder().setActivity(ACTIVITIES.get(0)).setEventTime(createEventInterval(LocalDateTime.of(2015, 2, 22, 9, 0))).build(),
                new TestEventBuilder().setActivity(ACTIVITIES.get(1)).setEventTime(createEventInterval(LocalDateTime.of(2015, 2, 22, 12, 0))).build(),
                new TestEventBuilder().setActivity(ACTIVITIES.get(7)).setEventTime(createEventInterval(LocalDateTime.of(2015, 2, 22, 14, 0))).build(),
                new TestEventBuilder().setActivity(ACTIVITIES.get(11)).setEventTime(createEventInterval(LocalDateTime.of(2015, 2, 22, 16, 0))).build(),
                new TestEventBuilder().setActivity(ACTIVITIES.get(5)).setEventTime(createEventInterval(LocalDateTime.of(2015, 2, 22, 19, 0))).build(),
                new TestEventBuilder().setActivity(ACTIVITIES.get(8)).setEventTime(createEventInterval(LocalDateTime.of(2015, 2, 22, 21, 0))).build(),
                new TestEventBuilder().setActivity(ACTIVITIES.get(10)).setEventTime(createEventInterval(LocalDateTime.of(2015, 2, 23, 9, 0))).build(),
                new TestEventBuilder().setActivity(ACTIVITIES.get(4)).setEventTime(createEventInterval(LocalDateTime.of(2015, 2, 23, 12, 0))).build(),
                new TestEventBuilder().setActivity(ACTIVITIES.get(9)).setEventTime(createEventInterval(LocalDateTime.of(2015, 2, 23, 14, 0))).build(),
                new TestEventBuilder().setActivity(ACTIVITIES.get(6)).setEventTime(createEventInterval(LocalDateTime.of(2015, 2, 23, 16, 0))).build());
    }

    private static Range<LocalDateTime> createEventInterval(LocalDateTime startTime) {
        return Range.of(startTime, startTime.plusHours(2));
    }
}