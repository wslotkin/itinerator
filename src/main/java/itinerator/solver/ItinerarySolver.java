package itinerator.solver;

import cz.cvut.felk.cig.jcop.problem.Configuration;
import cz.cvut.felk.cig.jcop.solver.Solver;
import itinerator.datamodel.Itinerary;
import itinerator.itinerary.ItineraryFactory;

import static com.google.common.collect.Iterables.getOnlyElement;

public class ItinerarySolver {

    private final Solver solver;
    private final ItineraryProblem problem;
    private final ItineraryFactory itineraryFactory;

    public ItinerarySolver(Solver solver, ItineraryProblem problem, ItineraryFactory itineraryFactory) {
        this.solver = solver;
        this.problem = problem;
        this.itineraryFactory = itineraryFactory;
    }

    public void run() {
        solver.run();
    }

    public SolverResult getBestResult() {
        Configuration bestConfiguration = getOnlyElement(solver.getResult().getResultEntries()).getBestConfiguration();
        Itinerary bestItinerary = itineraryFactory.create(bestConfiguration);
        double score = problem.getDefaultFitness().getValue(bestConfiguration);
        return new SolverResult(bestItinerary, score);
    }

    public static class SolverResult {
        private final Itinerary itinerary;
        private final double score;

        public SolverResult(Itinerary itinerary, double score) {
            this.itinerary = itinerary;
            this.score = score;
        }

        public Itinerary getItinerary() {
            return itinerary;
        }

        public double getScore() {
            return score;
        }
    }
}
