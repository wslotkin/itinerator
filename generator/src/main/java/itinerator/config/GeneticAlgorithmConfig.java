package itinerator.config;

public class GeneticAlgorithmConfig {
    private final double mutationRate;
    private final int populationSize;
    private final int maxIterations;
    private final long maxDuration;
    private final boolean parallelized;

    public GeneticAlgorithmConfig(double mutationRate,
                                  int populationSize,
                                  int maxIterations,
                                  long maxDuration,
                                  boolean parallelized) {
        this.mutationRate = mutationRate;
        this.populationSize = populationSize;
        this.maxIterations = maxIterations;
        this.maxDuration = maxDuration;
        this.parallelized = parallelized;
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

    public boolean getParallelized() {
        return parallelized;
    }

    public Builder toBuilder() {
        return new Builder()
                .setMutationRate(mutationRate)
                .setPopulationSize(populationSize)
                .setMaxIterations(maxIterations)
                .setMaxDuration(maxDuration)
                .setParallelized(parallelized);
    }

    public static class Builder {
        private double mutationRate = 0.2;
        private int populationSize = 1000;
        private int maxIterations = 100;
        private long maxDuration = Integer.MAX_VALUE;
        private boolean parallelized = true;

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

        public Builder setParallelized(boolean parallelized) {
            this.parallelized = parallelized;
            return this;
        }

        public GeneticAlgorithmConfig build() {
            return new GeneticAlgorithmConfig(mutationRate,
                    populationSize,
                    maxIterations,
                    maxDuration,
                    parallelized);
        }
    }
}
