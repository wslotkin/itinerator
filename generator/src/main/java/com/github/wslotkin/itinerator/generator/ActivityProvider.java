package com.github.wslotkin.itinerator.generator;

import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.datamodel.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ActivityProvider {
    default List<Activity> getActivities() {
        try {
            return doGetActivities();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    List<Activity> doGetActivities() throws IOException;

    default List<Event> getFixedEvents() {
        return new ArrayList<>();
    }
}
