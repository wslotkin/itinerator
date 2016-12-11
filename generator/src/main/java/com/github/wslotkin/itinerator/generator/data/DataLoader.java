package com.github.wslotkin.itinerator.generator.data;

import com.github.wslotkin.itinerator.generator.datamodel.Activity;

import java.io.IOException;
import java.util.List;

public interface DataLoader {
    List<Activity> loadData(String filename) throws IOException;
}
