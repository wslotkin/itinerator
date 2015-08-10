package itinerator.data;

import itinerator.datamodel.Activity;

import java.io.IOException;
import java.util.List;

public interface DataLoader {
    List<Activity> loadData(String filename) throws IOException;
}
