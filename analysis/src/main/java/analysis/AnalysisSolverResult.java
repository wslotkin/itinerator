package analysis;

import itinerator.solver.ItinerarySolver;

class AnalysisSolverResult {
    private final String id;
    private final ItinerarySolver.SolverResult result;
    private final double referenceScore;

    public AnalysisSolverResult(String id, ItinerarySolver.SolverResult result, double referenceScore) {
        this.id = id;
        this.result = result;
        this.referenceScore = referenceScore;
    }

    public String getId() {
        return id;
    }

    public ItinerarySolver.SolverResult getResult() {
        return result;
    }

    public double getReferenceScore() {
        return referenceScore;
    }
}
