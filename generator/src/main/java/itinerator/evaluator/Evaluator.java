package itinerator.evaluator;

import java.util.function.ToDoubleFunction;

@FunctionalInterface
public interface Evaluator<T> extends ToDoubleFunction<T> {
    @SafeVarargs
    static <T> Evaluator<T> compose(Evaluator<T> first, Evaluator<T>... rest) {
        Evaluator<T> finalResult = first;
        for (Evaluator<T> next : rest) {
            final Evaluator<T> currentResult = finalResult;
            finalResult = input -> currentResult.applyAsDouble(input) + next.applyAsDouble(input);
        }
        return finalResult;
    }
}
