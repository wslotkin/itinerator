package itinerator.config;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@Immutable
public interface GeneticAlgorithmConfig {
    @Default
    default double getMutationRate() {
        return 0.2;
    }

    @Default
    default int getPopulationSize() {
        return 1000;
    }

    @Default
    default int getMaxIterations() {
        return 100;
    }

    @Default
    default long getMaxDuration() {
        return Integer.MAX_VALUE;
    }

    @Default
    default boolean getParallelized() {
        return true;
    }
}
