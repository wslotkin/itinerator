package com.github.wslotkin.itinerator.generator.evaluator;

import com.github.wslotkin.itinerator.generator.config.EvaluationConfig;
import com.github.wslotkin.itinerator.generator.datamodel.ActivityType;
import com.github.wslotkin.itinerator.generator.datamodel.Event;
import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;
import com.github.wslotkin.itinerator.generator.itinerary.TimeUtil;
import com.google.common.annotations.VisibleForTesting;

import static com.github.wslotkin.itinerator.generator.evaluator.EvaluatorFactory.evaluator;
import static com.github.wslotkin.itinerator.generator.evaluator.EvaluatorType.*;
import static java.lang.Math.pow;

class EventEvaluators implements Evaluator<Itinerary> {
    @VisibleForTesting
    static final int FUN_NORMALIZATION_FACTOR = 5;

    private final Evaluator<Event> evaluator;

    public static Evaluator<Itinerary> eventEvaluators(EvaluationConfig config, EvaluatorState evaluatorState) {
        return new EventEvaluators(evaluator(funEvaluator(), evaluatorState, FUN)
                .andThen(evaluator(costEvaluator(config.getCostPenalty()), evaluatorState, COST))
                .andThen(evaluator(hoursEvaluator(config.getInvalidHoursPenalty()), evaluatorState, HOURS))
                .andThen(evaluator(movementEvaluator(config.getAreaHoppingPenalty(), config.getAreaHoppingThreshold()), evaluatorState, MOVEMENT))
                .andThen(evaluator(sleepEvaluator(config.getMissingSleepMinutesPenalty()), evaluatorState, SLEEP))
                .andThen(evaluator(travelEvaluator(config.getTravelTimePenalty()), evaluatorState, TRAVEL)));
    }

    @VisibleForTesting
    EventEvaluators(Evaluator<Event> eventEvaluator) {
        this.evaluator = eventEvaluator;
    }

    @Override
    public double applyAsDouble(Itinerary itinerary) {
        return itinerary.getEvents().stream()
                .mapToDouble(evaluator)
                .sum();
    }

    @VisibleForTesting
    static Evaluator<Event> funEvaluator() {
        return event -> pow(event.getActivity().getScore(), 2) * event.getActivity().getDuration() / FUN_NORMALIZATION_FACTOR;
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
        return event -> event.getActivity().getType() == ActivityType.SLEEP
                ? missingSleepMinutesPenalty * Math.max(TimeUtil.TARGET_MINUTES_OF_SLEEP - event.getActivity().getDuration(), 0)
                : 0.0;
    }

    @VisibleForTesting
    static Evaluator<Event> travelEvaluator(double travelTimePenalty) {
        return event -> travelTimePenalty * event.getTravelTime();
    }
}
