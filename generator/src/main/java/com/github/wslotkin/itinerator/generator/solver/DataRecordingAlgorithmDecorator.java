package com.github.wslotkin.itinerator.generator.solver;

import com.github.wslotkin.itinerator.generator.config.EvaluationConfig;
import com.github.wslotkin.itinerator.generator.datamodel.Activity;
import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;
import com.github.wslotkin.itinerator.generator.evaluator.Evaluator;
import com.github.wslotkin.itinerator.generator.evaluator.EvaluatorState;
import com.github.wslotkin.itinerator.generator.evaluator.EvaluatorType;
import com.github.wslotkin.itinerator.generator.evaluator.ItineraryFitness;
import com.github.wslotkin.itinerator.generator.itinerary.ItineraryFactory;
import com.google.common.annotations.VisibleForTesting;
import cz.cvut.felk.cig.jcop.algorithm.Algorithm;
import cz.cvut.felk.cig.jcop.algorithm.CannotContinueException;
import cz.cvut.felk.cig.jcop.algorithm.InvalidProblemException;
import cz.cvut.felk.cig.jcop.problem.Configuration;
import cz.cvut.felk.cig.jcop.problem.Fitness;
import cz.cvut.felk.cig.jcop.problem.ObjectiveProblem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static com.github.wslotkin.itinerator.generator.evaluator.ItineraryEvaluatorFactory.createEvaluators;
import static com.google.common.base.Throwables.propagate;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

class DataRecordingAlgorithmDecorator implements Algorithm {
    private static final String DELIMITER = ",";

    private final Algorithm delegate;
    private final EvaluatorState evaluatorState;
    private final ItineraryFitness itineraryFitness;
    private final BufferedWriter writer;

    public static Algorithm wrapWithDataRecordingAlgorithm(Algorithm delegate,
                                                           EvaluationConfig evaluationConfig,
                                                           List<Activity> availableActivities,
                                                           ItineraryFactory itineraryFactory,
                                                           String outputFilename) {
        EvaluatorState evaluatorState = new EvaluatorState();
        Evaluator<Itinerary> itineraryEvaluator = createEvaluators(evaluationConfig, availableActivities, evaluatorState);
        ItineraryFitness itineraryFitness = new ItineraryFitness(itineraryEvaluator, itineraryFactory);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFilename));
        } catch (IOException e) {
            throw propagate(e);
        }
        return new DataRecordingAlgorithmDecorator(delegate, evaluatorState, itineraryFitness, writer);
    }

    @VisibleForTesting
    DataRecordingAlgorithmDecorator(Algorithm delegate,
                                    EvaluatorState evaluatorState,
                                    ItineraryFitness itineraryFitness,
                                    BufferedWriter writer) {
        this.delegate = delegate;
        this.evaluatorState = evaluatorState;
        this.itineraryFitness = itineraryFitness;
        this.writer = writer;

        String header = stream(EvaluatorType.values()).map(EvaluatorType::name).collect(joining(DELIMITER));
        writeLine(header);
    }

    @Override
    public void init(ObjectiveProblem problem) throws InvalidProblemException {
        delegate.init(problem);
    }

    @Override
    public void optimize() throws CannotContinueException {
        delegate.optimize();

        evaluatorState.clear();
        itineraryFitness.getValue(delegate.getBestConfiguration());

        String newLine = evaluatorState.getScoreByEvaluator().values().stream().map(String::valueOf).collect(joining(DELIMITER));
        writeLine(newLine);
    }

    @Override
    public Configuration getBestConfiguration() {
        return delegate.getBestConfiguration();
    }

    @Override
    public void cleanUp() {
        delegate.cleanUp();
    }

    @Override
    public void setFitness(Fitness fitness) {
        delegate.setFitness(fitness);
    }

    @Override
    public double getBestFitness() {
        return delegate.getBestFitness();
    }

    @Override
    public void setLabel(String label) {
        delegate.setLabel(label);
    }

    @Override
    public String getLabel() {
        return delegate.getLabel();
    }

    private void writeLine(String line) {
        try {
            writer.write(line);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
