package com.github.wslotkin.itinerator.generator.itinerary;

import com.github.wslotkin.itinerator.generator.datamodel.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

public class SubitineraryProviderTest {

    private static final LocalDateTime T_0 = LocalDateTime.now();
    private static final LocalDateTime T_1 = T_0.plusHours(1);
    private static final LocalDateTime T_2 = T_1.plusHours(1);
    private static final LocalDateTime T_3 = T_2.plusHours(1);
    private static final LocalDateTime T_4 = T_3.plusHours(1);
    private static final LocalDateTime T_5 = T_4.plusHours(1);
    private static final LocalDateTime T_6 = T_5.plusHours(1);
    private static final LocalDateTime T_7 = T_6.plusHours(1);
    private static final Event EVENT_0 = ImmutableEvent.builder().eventTime(Range.of(T_0, T_1)).build();
    private static final Event EVENT_1 = ImmutableEvent.builder().eventTime(Range.of(T_1, T_2)).build();
    private static final Event EVENT_2 = ImmutableEvent.builder().eventTime(Range.of(T_2, T_3)).build();
    private static final Event EVENT_3 = ImmutableEvent.builder().eventTime(Range.of(T_3, T_4)).build();
    private static final Event EVENT_4 = ImmutableEvent.builder().eventTime(Range.of(T_4, T_5)).build();
    private static final Event EVENT_5 = ImmutableEvent.builder().eventTime(Range.of(T_5, T_6)).build();
    private static final Event EVENT_6 = ImmutableEvent.builder().eventTime(Range.of(T_6, T_7)).build();
    private static final Itinerary ITINERARY = ImmutableItinerary.of(newArrayList(EVENT_0, EVENT_1, EVENT_2, EVENT_3, EVENT_4, EVENT_5, EVENT_6));

    @Test
    public void shouldReturnAllEventsThatOverlapWithT2ThroughT5() {
        Itinerary subitinerary = SubitineraryProvider.subitinerary(ITINERARY, T_2, T_5);
        List<Event> subitineraryEvents = subitinerary.getEvents();

        assertEquals(4, subitineraryEvents.size());
        assertFalse(subitineraryEvents.contains(EVENT_0));
        assertFalse(subitineraryEvents.contains(EVENT_1));
        assertTrue(subitineraryEvents.contains(EVENT_2));
        assertTrue(subitineraryEvents.contains(EVENT_3));
        assertTrue(subitineraryEvents.contains(EVENT_4));
        assertTrue(subitineraryEvents.contains(EVENT_5));
        assertFalse(subitineraryEvents.contains(EVENT_6));
    }
}