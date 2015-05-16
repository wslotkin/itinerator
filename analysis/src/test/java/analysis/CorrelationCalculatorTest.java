package analysis;

import analysis.CorrelationCalculator.Point;
import org.junit.Test;

import static analysis.CorrelationCalculator.calculateCorrelation;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class CorrelationCalculatorTest {
    private static final double EPSILON = 1e-9;

    @Test
    public void perfectlyCorrelated() {
        double result = calculateCorrelation(newArrayList(new Point(0, 0),
                new Point(1, 1),
                new Point(2, 2)));

        assertEquals(result, 1.0, EPSILON);
    }

    @Test
    public void perfectlyInverselyCorrelated() {
        double result = calculateCorrelation(newArrayList(new Point(0, 2),
                new Point(1, 1),
                new Point(2, 0)));

        assertEquals(result, -1.0, 1e-9);
    }

    @Test
    public void completelyUncorrelated() {
        double result = calculateCorrelation(newArrayList(new Point(0, 1),
                new Point(1, 2),
                new Point(2, 1)));

        assertEquals(result, 0.0, 1e-9);
    }

    @Test
    public void calculatesCorrelation() {
        double result = calculateCorrelation(newArrayList(new Point(1.2, 2.3),
                new Point(3.2, 6.5),
                new Point(4.5, 2.1),
                new Point(5.6, 3.4),
                new Point(5.4, 4.3)));

        assertEquals(result, 0.095346724, 1e-9);
    }
}