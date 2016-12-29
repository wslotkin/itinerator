package com.github.wslotkin.itinerator.generator.evaluator;

class EvaluatorFactory {
    public static <T> Evaluator<T> evaluator(Evaluator<T> evaluator, EvaluatorState evaluatorState, EvaluatorType evaluatorType) {
        return evaluatorState != null
                ? new TrackingEvaluator<>(evaluator, evaluatorState, evaluatorType)
                : evaluator;
    }
}
