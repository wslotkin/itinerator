package itinerator.config;

import org.joda.time.DateTime;

public class ItineratorConfig {
    public static final String[] NYC_DATA = {"nycplaces.csv"};
    public static final String[] BEIJING_DATA = {"beijingspots.csv", "beijingrestaurants.csv"};
    public static final String[] SCRAPED_CHICAGO = {"scraped-data_updated_chicago_fixed.txt", "usa_chicago_restaurants_fixed.txt"};
    public static final String[] SCRAPED_BARCELONA = {"spain_barcelona_updated.txt"};

    private final DateTime startTime;
    private final DateTime endTime;
    private final String[] inputDataFiles;
    private final String outputFile;

    public ItineratorConfig(DateTime startTime,
                            DateTime endTime,
                            String[] inputDataFiles,
                            String outputFile) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.inputDataFiles = inputDataFiles;
        this.outputFile = outputFile;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public String[] getInputDataFiles() {
        return inputDataFiles;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public Builder toBuilder() {
        return new Builder()
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setInputDataFiles(inputDataFiles)
                .setOutputFile(outputFile);
    }

    public static class Builder {
        private DateTime startTime = new DateTime().withTime(19, 0, 0, 0);
        private DateTime endTime = startTime.plusDays(2);
        private String[] inputDataFiles = BEIJING_DATA;
        private String outputFile = "itinerary.txt";

        public Builder setStartTime(DateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(DateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder setInputDataFiles(String[] inputDataFiles) {
            this.inputDataFiles = inputDataFiles;
            return this;
        }

        public Builder setOutputFile(String outputFile) {
            this.outputFile = outputFile;
            return this;
        }

        public ItineratorConfig build() {
            return new ItineratorConfig(startTime,
                    endTime,
                    inputDataFiles,
                    outputFile
            );
        }
    }
}
