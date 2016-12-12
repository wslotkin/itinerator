package com.github.wslotkin.itinerator.analysis.custom;

import com.github.wslotkin.itinerator.analysis.custom.data.CustomItineraryLoader;
import com.github.wslotkin.itinerator.generator.ItineraryGenerator;
import com.github.wslotkin.itinerator.generator.config.EvaluationConfig;
import com.github.wslotkin.itinerator.generator.data.FileType;
import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.datamodel.Event;
import com.github.wslotkin.itinerator.generator.datamodel.SolverResult;
import com.google.common.collect.Iterables;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.github.wslotkin.itinerator.generator.solver.ItinerarySolver.generateResult;

class CustomItineraryGenerator implements ItineraryGenerator {
    private static final int BUFFER_PERIOD_MINUTES = 25;

    private final String inputFilename;
    private final FileType inputFileType;
    private final EvaluationConfig evaluationConfig;

    public CustomItineraryGenerator(String fileBase,
                                    FileType inputFileType,
                                    EvaluationConfig evaluationConfig) {
        this.inputFileType = inputFileType;
        this.evaluationConfig = evaluationConfig;
        inputFilename = fileBase + inputFileType.getExtenstion();
    }

    @Override
    public SolverResult getResult(List<Activity> activities, LocalDateTime startTime, LocalDateTime endTime) {
        List<Event> events = getEvents(activities);

        LocalDateTime start = events.get(0).getEventTime().getStart();
        LocalDateTime end = Iterables.getLast(events).getEventTime().getEnd().plusMinutes(BUFFER_PERIOD_MINUTES);

        return generateResult(activities, start, end, events, evaluationConfig);
    }

    private List<Event> getEvents(List<Activity> activities) {
        try {
            return new CustomItineraryLoader(activities).getActivities(inputFilename, inputFileType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
