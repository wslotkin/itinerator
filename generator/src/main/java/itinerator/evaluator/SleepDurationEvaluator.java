package itinerator.evaluator;

import itinerator.datamodel.Event;
import itinerator.datamodel.Itinerary;
import itinerator.itinerary.TimeUtil;

import static itinerator.datamodel.ActivityType.SLEEP;

public class SleepDurationEvaluator implements Evaluator {

    private final double missingSleepMinutesPenalty;

    public SleepDurationEvaluator(double missingSleepMinutesPenalty) {
        this.missingSleepMinutesPenalty = missingSleepMinutesPenalty;
    }

    @Override
    public double evaluate(Itinerary itinerary) {
        return missingSleepMinutesPenalty * itinerary.getEvents().stream()
                .map(Event::getActivity)
                .filter(activity -> activity.getType() == SLEEP)
                .mapToLong(activity -> Math.max(TimeUtil.TARGET_MINUTES_OF_SLEEP - activity.getDuration(), 0))
                .sum();
    }
}
