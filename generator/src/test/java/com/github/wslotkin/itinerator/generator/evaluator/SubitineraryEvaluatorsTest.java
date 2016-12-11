package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.datamodel.*;
import com.github.wslotkin.itinerator.generator.performance.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubitineraryEvaluatorsTest {
    private static final Event EVENT_1 = event("1");
    private static final Event EVENT_2 = event("2");
    private static final Itinerary ITINERARY = ImmutableItinerary.of(newArrayList(EVENT_1, EVENT_2));
    private static final Itinerary SUBITINERARY_1 = ImmutableItinerary.of(newArrayList(EVENT_1));
    private static final Itinerary SUBITINERARY_2 = ImmutableItinerary.of(newArrayList(EVENT_2));
    private static final double EVALUATOR_1_SUBITINERARY_1_SCORE = 1.2;
    private static final double EVALUATOR_1_SUBITINERARY_2_SCORE = 4.3;

    private Evaluator<Itinerary> firstEvaluator;
    private DaySubitineraryProvider subitineraryProvider;

    @Before
    public void before() {
        firstEvaluator = TestUtil.mockGeneric(Evaluator.class);
        subitineraryProvider = mock(DaySubitineraryProvider.class);

        when(firstEvaluator.applyAsDouble(SUBITINERARY_1)).thenReturn(EVALUATOR_1_SUBITINERARY_1_SCORE);
        when(firstEvaluator.applyAsDouble(SUBITINERARY_2)).thenReturn(EVALUATOR_1_SUBITINERARY_2_SCORE);
        when(subitineraryProvider.getPerDaySubitineraries(ITINERARY)).thenReturn(newArrayList(SUBITINERARY_1, SUBITINERARY_2));
    }

    @Test
    public void shouldReturnSumOfEachSubitineraryScore() {
        SubitineraryEvaluators compositeEvaluator = new SubitineraryEvaluators(subitineraryProvider, firstEvaluator);
        double result = compositeEvaluator.applyAsDouble(ITINERARY);

        double expectedResult = EVALUATOR_1_SUBITINERARY_1_SCORE + EVALUATOR_1_SUBITINERARY_2_SCORE;

        Assert.assertEquals(expectedResult, result, TestUtil.DELTA);
    }

    private static Event event(String id) {
        return ImmutableEvent.builder().activity(ImmutableActivity.builder().id(id).build()).build();
    }
}