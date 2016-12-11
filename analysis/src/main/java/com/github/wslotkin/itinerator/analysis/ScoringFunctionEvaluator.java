package com.github.wslotkin.itinerator.analysis;

import java.util.ArrayList;
import java.util.List;

import static com.github.wslotkin.itinerator.analysis.CorrelationCalculator.calculateCorrelation;

class ScoringFunctionEvaluator {

    public double correlationToReferenceValues(Iterable<AnalysisSolverResult> results) {
        List<CorrelationCalculator.Point> dataPoints = new ArrayList<>();
        for (AnalysisSolverResult result : results) {
            dataPoints.add(new CorrelationCalculator.Point(result.getResult().getScore(), result.getReferenceScore()));
        }

        return calculateCorrelation(dataPoints);
    }
}
