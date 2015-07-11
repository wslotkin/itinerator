package itinerator.config;

public class GeneticAlgorithmConfig {
    private final double mutationRate;
    private final int populationSize;
    private final int maxIterations;
    private final long maxDuration;
    private final int numberOfThreads;

    public GeneticAlgorithmConfig(double mutationRate,
                                  int populationSize,
                                  int maxIterations,
                                  long maxDuration,
                                  int numberOfThreads) {
        this.mutationRate = mutationRate;
        this.populationSize = populationSize;
        this.maxIterations = maxIterations;
        this.maxDuration = maxDuration;
        this.numberOfThreads = numberOfThreads;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public Builder toBuilder() {
        return new Builder()
                .setMutationRate(mutationRate)
                .setPopulationSize(populationSize)
                .setMaxIterations(maxIterations)
                .setMaxDuration(maxDuration)
                .setNumberOfThreads(numberOfThreads);
    }

    public static class Builder {
        private double mutationRate = 0.2;
        private int populationSize = 1000;
        private int maxIterations = 100;
        private long maxDuration = Integer.MAX_VALUE;
        private int numberOfThreads = 2;

        public Builder setMutationRate(double mutationRate) {
            this.mutationRate = mutationRate;
            return this;
        }

        public Builder setPopulationSize(int populationSize) {
            this.populationSize = populationSize;
            return this;
        }

        public Builder setMaxIterations(int maxIterations) {
            this.maxIterations = maxIterations;
            return this;
        }

        public Builder setMaxDuration(long maxDuration) {
            this.maxDuration = maxDuration;
            return this;
        }

        public Builder setNumberOfThreads(int numberOfThreads) {
            this.numberOfThreads = numberOfThreads;
            return this;
        }

        public GeneticAlgorithmConfig build() {
            return new GeneticAlgorithmConfig(mutationRate,
                    populationSize,
                    maxIterations,
                    maxDuration,
                    numberOfThreads);
        }
    }
}
