package itinerator.data;

import java.io.BufferedReader;
import java.io.IOException;

import static itinerator.data.FileType.CSV;

class CsvItineraryLoader implements CustomItineraryLoader.CustomDataLoader {

    private final BufferedReader reader;

    public CsvItineraryLoader(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public EventInputs getNextInputs() throws IOException {
        String inputLine = reader.readLine();
        if (inputLine != null) {
            String[] elements = inputLine.split(CSV.getDelimiter());
            String activityId = elements[0];
            String type = elements[1];
            String start = elements[2];
            String end = elements[3];
            return new EventInputs(activityId, start, end, type);
        } else {
            return null;
        }
    }
}
