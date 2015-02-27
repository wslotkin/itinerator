package itinerator.solver;

import cz.cvut.felk.cig.jcop.problem.Configuration;
import cz.cvut.felk.cig.jcop.result.ResultEntry;
import cz.cvut.felk.cig.jcop.solver.Solver;
import itinerator.datamodel.Itinerary;
import itinerator.itinerary.ItineraryFactory;

import static com.google.common.collect.Iterables.getOnlyElement;

public class ItinerarySolver {

    private final Solver solver;
    private final ItineraryFactory itineraryFactory;

    public ItinerarySolver(Solver solver, ItineraryFactory itineraryFactory) {
        this.solver = solver;
        this.itineraryFactory = itineraryFactory;
    }

    public void run() {
        solver.run();
    }

    public SolverResult getBestResult() {
        ResultEntry onlyResult = getOnlyElement(solver.getResult().getResultEntries());
        Configuration bestConfiguration = onlyResult.getBestConfiguration();
        Itinerary bestItinerary = itineraryFactory.create(bestConfiguration);
        double score = onlyResult.getBestFitness();
        long duration = onlyResult.getStopTimestamp().getClockTime() - onlyResult.getStartTimestamp().getClockTime();
        return new SolverResult(bestItinerary, bestConfiguration, score, duration);
    }

    public static class SolverResult {
        private final Itinerary itinerary;
        private final Configuration configuration;
        private final long duration;
        private final double score;

        public SolverResult(Itinerary itinerary, Configuration configuration, double score, long duration) {
            this.itinerary = itinerary;
            this.score = score;
            this.configuration = configuration;
            this.duration = duration;
        }

        public Itinerary getItinerary() {
            return itinerary;
        }

        public Configuration getConfiguration() {
            return configuration;
        }

        public double getScore() {
            return score;
        }

        public long getDuration() {
            return duration;
        }
    }
}
