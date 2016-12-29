package com.github.wslotkin.itinerator.generator.evaluator;

import java.util.EnumMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableMap;

public class EvaluatorState {
    private static final Double BOXED_ZERO = 0D;

    private final Map<EvaluatorType, Double> perEvaluatorScore;

    public EvaluatorState() {
        perEvaluatorScore = new EnumMap<>(EvaluatorType.class);
        stream(EvaluatorType.values()).forEach(type -> perEvaluatorScore.put(type, BOXED_ZERO));
    }

    public void update(EvaluatorType evaluatorType, double score) {
        Double currentScore = perEvaluatorScore.getOrDefault(evaluatorType, BOXED_ZERO);
        perEvaluatorScore.put(evaluatorType, currentScore + score);
    }

    public Map<EvaluatorType, Double> getScoreByEvaluator() {
        return unmodifiableMap(perEvaluatorScore);
    }

    public void clear() {
        perEvaluatorScore.entrySet().forEach(entry -> entry.setValue(0.0));
    }
}
