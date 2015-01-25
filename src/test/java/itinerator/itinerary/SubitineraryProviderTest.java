package itinerator.itinerary;

import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import itinerator.datamodel.TestEventBuilder;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

public class SubitineraryProviderTest {

    private static final DateTime T_0 = new DateTime();
    private static final DateTime T_1 = T_0.plusHours(1);
    private static final DateTime T_2 = T_1.plusHours(1);
    private static final DateTime T_3 = T_2.plusHours(1);
    private static final DateTime T_4 = T_3.plusHours(1);
    private static final DateTime T_5 = T_4.plusHours(1);
    private static final DateTime T_6 = T_5.plusHours(1);
    private static final DateTime T_7 = T_6.plusHours(1);
    private static final Event EVENT_0 = new TestEventBuilder().setEventTime(new Interval(T_0, T_1)).build();
    private static final Event EVENT_1 = new TestEventBuilder().setEventTime(new Interval(T_1, T_2)).build();
    private static final Event EVENT_2 = new TestEventBuilder().setEventTime(new Interval(T_2, T_3)).build();
    private static final Event EVENT_3 = new TestEventBuilder().setEventTime(new Interval(T_3, T_4)).build();
    private static final Event EVENT_4 = new TestEventBuilder().setEventTime(new Interval(T_4, T_5)).build();
    private static final Event EVENT_5 = new TestEventBuilder().setEventTime(new Interval(T_5, T_6)).build();
    private static final Event EVENT_6 = new TestEventBuilder().setEventTime(new Interval(T_6, T_7)).build();
    private static final Itinerary ITINERARY = new Itinerary(newArrayList(EVENT_0, EVENT_1, EVENT_2, EVENT_3, EVENT_4, EVENT_5, EVENT_6));

    @Test
    public void shouldReturnAllEventsThatOverlapWithT2ThroughT5() {
        Itinerary subitinerary = SubitineraryProvider.subitinerary(ITINERARY, T_2, T_5);
        List<Event> subitineraryEvents = subitinerary.getEvents();

        assertEquals(5, subitineraryEvents.size());
        assertFalse(subitineraryEvents.contains(EVENT_0));
        assertTrue(subitineraryEvents.contains(EVENT_1));
        assertTrue(subitineraryEvents.contains(EVENT_2));
        assertTrue(subitineraryEvents.contains(EVENT_3));
        assertTrue(subitineraryEvents.contains(EVENT_4));
        assertTrue(subitineraryEvents.contains(EVENT_5));
        assertFalse(subitineraryEvents.contains(EVENT_6));
    }
}