package itinerator.evaluator;

import cz.cvut.felk.cig.jcop.problem.Configuration;
import itinerator.datamodel.Activity;
import itinerator.datamodel.Itinerary;
import org.joda.time.DateTime;

import java.util.List;

public class ItineraryFactory {

    private final List<Activity> activities;
    private final DateTime startTime;

    public ItineraryFactory(List<Activity> activities, DateTime startTime) {
        this.activities = activities;
        this.startTime = startTime;
    }

    public Itinerary create(Configuration configuration) {
        ItineraryBuilder builder = new ItineraryBuilder(startTime);
        for (int i = 0; i < activities.size(); i++) {
            Integer position = configuration.valueAt(i);
            if (position >= 0) {
                Activity activity = activities.get(i);
                builder.addActivityAtPosition(activity, position);
            }
        }
        return builder.build();
    }
}
