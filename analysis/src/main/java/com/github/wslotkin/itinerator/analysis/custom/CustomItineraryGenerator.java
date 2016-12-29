package com.github.wslotkin.itinerator.analysis.custom;

import com.github.wslotkin.itinerator.analysis.custom.data.CustomItineraryLoader;
import com.github.wslotkin.itinerator.generator.ItineraryGenerator;
import com.github.wslotkin.itinerator.generator.calculators.RoundingTravelTimeCalculator;
import com.github.wslotkin.itinerator.generator.config.EvaluationConfig;
import com.github.wslotkin.itinerator.generator.data.FileType;
import com.github.wslotkin.itinerator.generator.datamodel.*;
import com.github.wslotkin.itinerator.generator.itinerary.ItineraryFactory;
import com.github.wslotkin.itinerator.generator.solver.ItineraryProblem;
import com.google.common.collect.Iterables;
import cz.cvut.felk.cig.jcop.problem.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.wslotkin.itinerator.generator.calculators.RoundingTravelTimeCalculator.roundingTravelTimeCalculator;

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
    public SolverResult getResult(List<Activity> activities,
                                  List<Event> fixedEvents,
                                  LocalDateTime startTime,
                                  LocalDateTime endTime) {
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

    private static SolverResult generateResult(List<Activity> activities,
                                               LocalDateTime startTime,
                                               LocalDateTime endTime,
                                               List<Event> events,
                                               EvaluationConfig evaluationConfig) {
        RoundingTravelTimeCalculator travelTimeCalculator = roundingTravelTimeCalculator();
        ItineraryFactory itineraryFactory = new ItineraryFactory(activities, startTime, endTime, travelTimeCalculator, events);
        ItineraryProblem itineraryProblem = new ItineraryProblem(activities, startTime, endTime, itineraryFactory, evaluationConfig);
        List<Integer> attributes = activities.stream().map(activities::indexOf).collect(Collectors.toList());

        Configuration configuration = new Configuration(attributes);
        Itinerary itinerary = itineraryFactory.create(configuration);
        double score = itineraryProblem.getDefaultFitness().getValue(configuration);
        return ImmutableSolverResult.of(itinerary, configuration, score, 0L);
    }
}
