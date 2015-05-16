package custom.data;

import itinerator.itinerary.ItineraryFormatter;

import java.io.BufferedReader;
import java.io.IOException;

import static custom.data.FileType.TEXT;

class TextItineraryLoader implements CustomItineraryLoader.CustomDataLoader {

    public static final String HEADER_TEXT = "Optimization complete";
    private final BufferedReader reader;

    public TextItineraryLoader(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public EventInputs getNextInputs() throws IOException {
        String nextLine = getNextLine();
        if (nextLine != null && !nextLine.isEmpty()) {
            String[] elements = nextLine.split("-" + TEXT.getDelimiter());
            String idAndType = elements[0];
            String[] idAndTypeElements = idAndType.split("\\(");
            String id = idAndTypeElements[0].trim();
            String type = idAndTypeElements[1].split("\\)")[0];
            String timeRange = elements[1];
            String[] timeRangeElements = timeRange.split("\\[")[1].split("\\]")[0].split(" - ");
            String startTime = toStandardDateTimeString(timeRangeElements[0]);
            String endTime = toStandardDateTimeString(timeRangeElements[1]);

            return new EventInputs(id, startTime, endTime, type);
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

    private static String toStandardDateTimeString(String timeRangeElement) {
        return ItineraryFormatter.DATE_TIME_FORMATTER.parseDateTime(timeRangeElement).toString();
    }
}
