package itinerator.evaluator;

import com.google.common.annotations.VisibleForTesting;
import itinerator.config.EvaluationConfig;
import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;

import static itinerator.datamodel.ActivityType.SLEEP;
import static itinerator.evaluator.Evaluator.compose;
import static itinerator.itinerary.TimeUtil.TARGET_MINUTES_OF_SLEEP;
import static java.lang.Math.max;

class EventEvaluators implements Evaluator<Itinerary> {
    private final Evaluator<Event> evaluator;

    public static Evaluator<Itinerary> createEvaluators(EvaluationConfig config) {
        return new EventEvaluators(funEvaluator(),
                costEvaluator(config.getCostPenalty()),
                hoursEvaluator(config.getInvalidHoursPenalty()),
                movementEvaluator(config.getAreaHoppingPenalty(), config.getAreaHoppingThreshold()),
                sleepEvaluator(config.getMissingSleepMinutesPenalty()),
                travelEvaluator(config.getTravelTimePenalty()));
    }

    @SafeVarargs
    @VisibleForTesting
    EventEvaluators(Evaluator<Event> firstEvaluator, Evaluator<Event>... otherEvaluators) {
        this.evaluator = compose(firstEvaluator, otherEvaluators);
    }

    @Override
    public double applyAsDouble(Itinerary itinerary) {
        return itinerary.getEvents().stream()
                .mapToDouble(evaluator::applyAsDouble)
                .sum();
    }

    @VisibleForTesting
    static Evaluator<Event> funEvaluator() {
        return event -> event.getActivity().getScore() * event.getActivity().getDuration();
    }

    @VisibleForTesting
    static Evaluator<Event> costEvaluator(double costPenalty) {
        return event -> costPenalty * event.getActivity().getCost();
    }

    @VisibleForTesting
    static Evaluator<Event> hoursEvaluator(double invalidHoursPenalty) {
        return event -> !event.getActivity().getWeeklySchedule().isValid(event.getEventTime()) ? invalidHoursPenalty : 0.0;
    }

    @VisibleForTesting
    static Evaluator<Event> movementEvaluator(double areaHoppingPenalty, double areaHoppingThreshold) {
        return event -> event.getTravelTime() > areaHoppingThreshold ? areaHoppingPenalty : 0.0;
    }

    @VisibleForTesting
    static Evaluator<Event> sleepEvaluator(double missingSleepMinutesPenalty) {
        return event -> event.getActivity().getType() == SLEEP
                ? missingSleepMinutesPenalty * max(TARGET_MINUTES_OF_SLEEP - event.getActivity().getDuration(), 0)
                : 0.0;
    }

    @VisibleForTesting
    static Evaluator<Event> travelEvaluator(double travelTimePenalty) {
        return event -> travelTimePenalty * event.getTravelTime();
    }
}
