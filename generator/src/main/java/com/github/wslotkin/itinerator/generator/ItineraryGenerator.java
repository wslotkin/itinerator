package com.github.wslotkin.itinerator.generator;

import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.datamodel.SolverResult;

import java.time.LocalDateTime;
import java.util.List;

public interface ItineraryGenerator {
    SolverResult getResult(List<Activity> activities, LocalDateTime startTime, LocalDateTime endTime);
}
