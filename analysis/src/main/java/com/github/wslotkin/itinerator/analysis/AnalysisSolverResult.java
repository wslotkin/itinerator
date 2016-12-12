package com.github.wslotkin.itinerator.analysis;

import com.github.wslotkin.itinerator.generator.datamodel.SolverResult;

class AnalysisSolverResult {
    private final String id;
    private final SolverResult result;
    private final double referenceScore;

    public AnalysisSolverResult(String id, SolverResult result, double referenceScore) {
        this.id = id;
        this.result = result;
        this.referenceScore = referenceScore;
    }

    public String getId() {
        return id;
    }

    public SolverResult getResult() {
        return result;
    }

    public double getReferenceScore() {
        return referenceScore;
    }
}
