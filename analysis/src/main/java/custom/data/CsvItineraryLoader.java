package custom.data;

import java.io.BufferedReader;
import java.io.IOException;

import static itinerator.data.FileType.CSV;

class CsvItineraryLoader implements CustomItineraryLoader.CustomDataLoader {

    public static final String HEADER_TEXT = "Place,Type";

    private final BufferedReader reader;

    public CsvItineraryLoader(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public EventInputs getNextInputs() throws IOException {
        String inputLine = getNextLine();
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

    private String getNextLine() throws IOException {
        String nextLine = reader.readLine();
        if (nextLine != null && nextLine.contains(HEADER_TEXT)) {
            return getNextLine();
        } else {
            return nextLine;
        }
    }
}
