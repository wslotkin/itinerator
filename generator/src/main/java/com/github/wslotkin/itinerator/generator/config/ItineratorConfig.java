package com.github.wslotkin.itinerator.generator.config;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Immutable
public interface ItineratorConfig {
    String[] NYC_DATA = {"nycplaces.csv"};
    String[] BEIJING_DATA = {"beijingspots.csv", "beijingrestaurants.csv"};
    String[] SCRAPED_CHICAGO = {"scraped-data_updated_chicago_fixed.txt", "usa_chicago_restaurants_fixed.txt"};
    String[] SCRAPED_BARCELONA = {"spain_barcelona_updated.txt"};

    @Default
    default LocalDateTime getStartTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0));
    }

    @Default
    default LocalDateTime getEndTime() {
        return getStartTime().plusDays(2);
    }

    @Default
    default String[] getInputDataFiles() {
        return BEIJING_DATA;
    }

    @Default
    default String getOutputFile() {
        return "itinerary.txt";
    }

    Set<String> getExcludedActivityIds();
}
