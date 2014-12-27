package itinerator.evaluator;

import cz.cvut.felk.cig.jcop.problem.BaseFitness;
import cz.cvut.felk.cig.jcop.problem.Configuration;
import itinerator.datamodel.Activity;
import org.joda.time.DateTime;

import java.util.List;

public class ItineraryFitness extends BaseFitness {

    private final List<Activity> activities;
    private final ItineraryEvaluator evaluator;
    private final DateTime startTime;

    public ItineraryFitness(List<Activity> activities, ItineraryEvaluator evaluator, DateTime startTime) {
        this.activities = activities;
        this.evaluator = evaluator;
        this.startTime = startTime;
        maxFitness = activities.stream().mapToDouble(Activity::getScore).sum();
        minFitness = 0.0;
    }

    @Override
    public double getValue(Configuration configuration) {
        ItineraryBuilder builder = new ItineraryBuilder(startTime);
        for (int i = 0; i < activities.size(); i++) {
            Integer position = configuration.valueAt(i);
            if (position != 0) {
                Activity activity = activities.get(i);
                builder.addActivityAtPosition(activity, position);
            }
        }
        return evaluator.evaluate(builder.build());
    }
}
