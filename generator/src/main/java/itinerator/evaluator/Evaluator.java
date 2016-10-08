package itinerator.evaluator;

import java.util.function.ToDoubleFunction;

@FunctionalInterface
public interface Evaluator<T> extends ToDoubleFunction<T> {
    default Evaluator<T> andThen(Evaluator<T> nextEvaluator) {
        return input -> applyAsDouble(input) + nextEvaluator.applyAsDouble(input);
    }
}
