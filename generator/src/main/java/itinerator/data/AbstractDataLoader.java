package itinerator.data;

import itinerator.datamodel.Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractDataLoader implements DataLoader {

    private final FileType fileType;

    protected AbstractDataLoader(FileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public List<Activity> loadData(String filename) throws IOException {
        List<Activity> activities = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));

        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            activities.add(parseRowElements(line.split(fileType.getDelimiter())));
        }

        return activities;
    }

    protected abstract Activity parseRowElements(String[] rowElements);
}
