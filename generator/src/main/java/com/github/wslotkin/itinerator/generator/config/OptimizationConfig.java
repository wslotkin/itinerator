package com.github.wslotkin.itinerator.generator.config;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@Immutable
public interface OptimizationConfig {
    @Default
    default ItineratorConfig getItineratorConfig() {
        return ImmutableItineratorConfig.builder().build();
    }

    @Default
    default GeneticAlgorithmConfig getGeneticAlgorithmConfig() {
        return ImmutableGeneticAlgorithmConfig.builder().build();
    }

    @Default
    default EvaluationConfig getEvaluationConfig() {
        return ImmutableEvaluationConfig.builder().build();
    }
}
