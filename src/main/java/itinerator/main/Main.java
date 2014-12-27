package itinerator.main;

import itinerator.Itinerator;
import itinerator.data.DataLoader;
import itinerator.datamodel.Activity;
import itinerator.datamodel.Itinerary;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.io.IOException;
import java.util.Collection;

public class Main {

    public static void main(String[] args) throws IOException {
        Collection<Activity> activities = new DataLoader().loadData("beijingspots.csv");
        Itinerary result = new Itinerator().generate(activities, new Interval(new DateTime().minusDays(1).getMillis(), new DateTime().getMillis()));
        System.out.println("result: " + result);
    }
}
