package analysis;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

class CorrelationCalculator {

    public static double calculateCorrelation(List<Point> aDataPoints) {
        Statistics inputStats = calculateStats(aDataPoints.stream().map(Point::getX).collect(Collectors.toList()));
        Statistics outputStats = calculateStats(aDataPoints.stream().map(Point::getY).collect(Collectors.toList()));

        return aDataPoints.stream()
                .mapToDouble(point -> (point.getX() - inputStats.mean) * (point.getY() - outputStats.mean))
                .sum() / (inputStats.standardDeviation * outputStats.standardDeviation);
    }

    private static Statistics calculateStats(Collection<Double> values) {
        double average = values.stream().mapToDouble(a -> a).average().getAsDouble();
        double standardDeviation = calculateStandardDeviation(values, average);
        return new Statistics(average, standardDeviation);
    }

    private static double calculateStandardDeviation(Collection<Double> values, double mean) {
        double sumOfSquaredDifferencesFromMean = values.stream()
                .mapToDouble(value -> pow(value - mean, 2))
                .sum();
        return sqrt(sumOfSquaredDifferencesFromMean);
    }

    public static class Point {
        private final double x;
        private final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    private static class Statistics {
        private final double mean;
        private final double standardDeviation;

        private Statistics(double mean, double standardDeviation) {
            this.mean = mean;
            this.standardDeviation = standardDeviation;
        }
    }
}
