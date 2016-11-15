package itinerator.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class ItineratorConfig {
    public static final String[] NYC_DATA = {"nycplaces.csv"};
    public static final String[] BEIJING_DATA = {"beijingspots.csv", "beijingrestaurants.csv"};
    public static final String[] SCRAPED_CHICAGO = {"scraped-data_updated_chicago_fixed.txt", "usa_chicago_restaurants_fixed.txt"};
    public static final String[] SCRAPED_BARCELONA = {"spain_barcelona_updated.txt"};

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String[] inputDataFiles;
    private final String outputFile;
    private final Set<String> excludedActivityIds;

    public ItineratorConfig(LocalDateTime startTime,
                            LocalDateTime endTime,
                            String[] inputDataFiles,
                            String outputFile,
                            Set<String> excludedActivityIds) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.inputDataFiles = inputDataFiles;
        this.outputFile = outputFile;
        this.excludedActivityIds = excludedActivityIds;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String[] getInputDataFiles() {
        return inputDataFiles;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public Set<String> getExcludedActivityIds() {
        return excludedActivityIds;
    }

    public Builder toBuilder() {
        return new Builder()
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setInputDataFiles(inputDataFiles)
                .setOutputFile(outputFile);
    }

    public static class Builder {
        private LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0));
        private LocalDateTime endTime = startTime.plusDays(2);
        private String[] inputDataFiles = BEIJING_DATA;
        private String outputFile = "itinerary.txt";
        private Set<String> excludedActivityIds = new HashSet<>();

        public Builder setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(LocalDateTime endTime) {
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

        public Builder setExcludedActivityIds(Set<String> excludedActivityIds) {
            this.excludedActivityIds = excludedActivityIds;
            return this;
        }

        public ItineratorConfig build() {
            return new ItineratorConfig(startTime,
                    endTime,
                    inputDataFiles,
                    outputFile,
                    excludedActivityIds);
        }
    }
}
