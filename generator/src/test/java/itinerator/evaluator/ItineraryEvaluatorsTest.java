package itinerator.evaluator;

import itinerator.datamodel.ActivityBuilder;
import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import itinerator.datamodel.TestEventBuilder;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static itinerator.TestUtil.DELTA;
import static itinerator.TestUtil.mockGeneric;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItineraryEvaluatorsTest {
    private static final Event EVENT_1 = event("1");
    private static final Event EVENT_2 = event("2");
    private static final Itinerary ITINERARY = new Itinerary(newArrayList(EVENT_1, EVENT_2));
    private static final Itinerary SUBITINERARY_1 = new Itinerary(newArrayList(EVENT_1));
    private static final Itinerary SUBITINERARY_2 = new Itinerary(newArrayList(EVENT_2));
    private static final double EVALUATOR_1_SUBITINERARY_1_SCORE = 1.2;
    private static final double EVALUATOR_1_SUBITINERARY_2_SCORE = 4.3;
    private static final double EVALUATOR_2_SUBITINERARY_1_SCORE = 5.6;
    private static final double EVALUATOR_2_SUBITINERARY_2_SCORE = 8.7;

    private Evaluator<Itinerary> firstEvaluator;
    private Evaluator<Itinerary> secondEvaluator;
    private DaySubitineraryProvider subitineraryProvider;

    @Before
    public void before() {
        firstEvaluator = mockGeneric(Evaluator.class);
        secondEvaluator = mockGeneric(Evaluator.class);
        subitineraryProvider = mock(DaySubitineraryProvider.class);

        when(firstEvaluator.applyAsDouble(SUBITINERARY_1)).thenReturn(EVALUATOR_1_SUBITINERARY_1_SCORE);
        when(firstEvaluator.applyAsDouble(SUBITINERARY_2)).thenReturn(EVALUATOR_1_SUBITINERARY_2_SCORE);
        when(secondEvaluator.applyAsDouble(SUBITINERARY_1)).thenReturn(EVALUATOR_2_SUBITINERARY_1_SCORE);
        when(secondEvaluator.applyAsDouble(SUBITINERARY_2)).thenReturn(EVALUATOR_2_SUBITINERARY_2_SCORE);
        when(subitineraryProvider.getPerDaySubitineraries(ITINERARY)).thenReturn(newArrayList(SUBITINERARY_1, SUBITINERARY_2));
    }

    @Test
    public void whenOneEvaluatorShouldReturnResultOfEvaluator() {
        ItineraryEvaluators compositeEvaluator = new ItineraryEvaluators(subitineraryProvider, firstEvaluator);
        double result = compositeEvaluator.applyAsDouble(ITINERARY);

        double expectedResult = EVALUATOR_1_SUBITINERARY_1_SCORE + EVALUATOR_1_SUBITINERARY_2_SCORE;

        assertEquals(expectedResult, result, DELTA);
    }

    @Test
    public void whenMultipleEvaluatorShouldReturnSumOfResultsOfEvaluators() {
        ItineraryEvaluators compositeEvaluator = new ItineraryEvaluators(subitineraryProvider, firstEvaluator, secondEvaluator);
        double result = compositeEvaluator.applyAsDouble(ITINERARY);

        double expectedResult = EVALUATOR_1_SUBITINERARY_1_SCORE
                + EVALUATOR_1_SUBITINERARY_2_SCORE
                + EVALUATOR_2_SUBITINERARY_1_SCORE
                + EVALUATOR_2_SUBITINERARY_2_SCORE;

        assertEquals(expectedResult, result, DELTA);
    }

    private static Event event(String id) {
        return new TestEventBuilder().setActivity(new ActivityBuilder().setId(id).build()).build();
    }
}