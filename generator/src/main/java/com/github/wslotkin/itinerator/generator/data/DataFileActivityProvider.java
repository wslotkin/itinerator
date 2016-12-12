package com.github.wslotkin.itinerator.generator.data;

import com.github.wslotkin.itinerator.generator.ActivityProvider;
import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.main.FileBasedItineraryGeneratorRunner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataFileActivityProvider implements ActivityProvider {
    private final List<String> inputDataFiles;

    public DataFileActivityProvider(List<String> inputDataFiles) {
        this.inputDataFiles = inputDataFiles;
    }

    @Override
    public List<Activity> doGetActivities() throws IOException {
        return loadActivities(inputDataFiles);
    }

    private static List<Activity> loadActivities(List<String> dataFiles) throws IOException {
        List<Activity> activities = new ArrayList<>();
        for (String dataFile : dataFiles) {
            DataLoader dataLoader = createForFile(dataFile);
            URL dataFileUrl = FileBasedItineraryGeneratorRunner.class.getClassLoader().getResource(dataFile);
            if (dataFileUrl != null) {
                activities.addAll(dataLoader.loadData(dataFileUrl.getPath()));
            }
        }
        return activities;
    }

    private static DataLoader createForFile(String filename) {
        for (FileType fileType : FileType.values()) {
            if (filename.contains(fileType.getExtenstion())) {
                return create(fileType);
            }
        }
        throw new IllegalArgumentException("Unrecognized input file: " + filename);
    }

    private static DataLoader create(FileType fileType) {
        switch (fileType) {
            case CSV:
                return new CsvDataLoader();
            case TEXT:
                return new TextDataLoader();
            default:
                throw new IllegalArgumentException("Unexpected file type: " + fileType);
        }
    }
}
