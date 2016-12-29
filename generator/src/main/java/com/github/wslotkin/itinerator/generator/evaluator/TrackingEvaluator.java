package com.github.wslotkin.itinerator.generator.evaluator;

class TrackingEvaluator<T> implements Evaluator<T> {
    private final Evaluator<T> delegate;
    private final EvaluatorState evaluatorState;
    private final EvaluatorType evaluatorType;

    public TrackingEvaluator(Evaluator<T> delegate, EvaluatorState evaluatorState, EvaluatorType evaluatorType) {
        this.delegate = delegate;
        this.evaluatorState = evaluatorState;
        this.evaluatorType = evaluatorType;
    }

    @Override
    public double applyAsDouble(T value) {
        double score = delegate.applyAsDouble(value);

        evaluatorState.update(evaluatorType, score);

        return score;
    }
}
