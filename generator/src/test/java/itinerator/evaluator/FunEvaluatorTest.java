package itinerator.evaluator;

import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import itinerator.datamodel.ActivityBuilder;
import itinerator.datamodel.TestEventBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static itinerator.TestConstants.DELTA;
import static org.junit.Assert.assertEquals;

public class FunEvaluatorTest {

    private static final long FIRST_ACTIVITY_DURATION = 10L;
    private static final long SECOND_ACTIVITY_DURATION = 20L;
    private static final double FIRST_ACTIVITY_SCORE = 1.2;
    private static final double SECOND_ACTIVITY_SCORE = 2.3;
    private static final Event FIRST_EVENT = createEvent(FIRST_ACTIVITY_DURATION, FIRST_ACTIVITY_SCORE);
    private static final Event SECOND_EVENT = createEvent(SECOND_ACTIVITY_DURATION, SECOND_ACTIVITY_SCORE);

    private FunEvaluator funEvaluator;

    @Before
    public void before() {
        funEvaluator = new FunEvaluator();
    }

    @Test
    public void whenItineraryIsEmptyShouldReturnZero() {
        double result = funEvaluator.evaluate(new Itinerary(new ArrayList<>()));

        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void whenItineraryHasOnlyOneEventReturnsScoreTimesDurationOfEvent() {
        double result = funEvaluator.evaluate(new Itinerary(newArrayList(FIRST_EVENT)));

        double expectedResult = FIRST_ACTIVITY_DURATION * FIRST_ACTIVITY_SCORE;

        assertEquals(expectedResult, result, DELTA);
    }

    @Test
    public void returnsSumOfScoreTimesDurationOfAllActivities() {
        double result = funEvaluator.evaluate(new Itinerary(newArrayList(FIRST_EVENT, SECOND_EVENT)));

        double expectedResult = FIRST_ACTIVITY_DURATION * FIRST_ACTIVITY_SCORE
                + SECOND_ACTIVITY_DURATION * SECOND_ACTIVITY_SCORE;

        assertEquals(expectedResult, result, DELTA);
    }

    private static Event createEvent(long activityDuration, double activityScore) {
        return new TestEventBuilder()
                .setActivity(new ActivityBuilder()
                        .setDuration(activityDuration)
                        .setScore(activityScore)
                        .build())
                .build();
    }
}