/*
 * Copyright © 2010 by Ondrej Skalicka. All Rights Reserved
 */

package itinerator.solver;

import cz.cvut.felk.cig.jcop.algorithm.CannotContinueException;
import cz.cvut.felk.cig.jcop.algorithm.InvalidProblemException;
import cz.cvut.felk.cig.jcop.algorithm.geneticalgorithm.Chromosome;
import cz.cvut.felk.cig.jcop.algorithm.geneticalgorithm.GeneticAlgorithm;
import cz.cvut.felk.cig.jcop.algorithm.geneticalgorithm.Selection;
import cz.cvut.felk.cig.jcop.problem.ObjectiveProblem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class ConcurrentGeneticAlgorithm extends GeneticAlgorithm {

    private final ExecutorService executorService;
    private final List<MutableGeneticAlgorithm> internalAlgorithms;

    public ConcurrentGeneticAlgorithm(int populationSize,
                                      double mutationRate,
                                      ExecutorService executorService,
                                      int parallelism) {
        super(populationSize, mutationRate);
        this.executorService = executorService;
        internalAlgorithms = new ArrayList<>(Math.max(1, parallelism));
        int populationLeftToAllocate = populationSize;
        for (int i = parallelism; i > 0; i--) {
            int allocatedPopulation = (populationLeftToAllocate / i) + (populationLeftToAllocate / i % 2);
            internalAlgorithms.add(new MutableGeneticAlgorithm(allocatedPopulation, mutationRate));
            populationLeftToAllocate -= allocatedPopulation;
        }
    }

    @Override
    public void init(ObjectiveProblem problem) throws InvalidProblemException {
        super.init(problem);
        population.clear();
        bestConfiguration = null;
        SelectionAdapter selectionAdapter = new SelectionAdapter(selection);
        for (MutableGeneticAlgorithm internalAlgorithm : internalAlgorithms) {
            internalAlgorithm.init(problem);

            if (bestConfiguration == null || internalAlgorithm.getBestFitness() > bestFitness) {
                bestFitness = internalAlgorithm.getBestFitness();
                bestConfiguration = internalAlgorithm.getBestConfiguration();
            }
            population.addAll(internalAlgorithm.getPopulation());

            internalAlgorithm.setSelection(selectionAdapter);
        }
    }

    @Override
    public void optimize() throws CannotContinueException {
        List<Chromosome> newGeneration = new ArrayList<>(populationSize);
        selection.init(population);

        List<Callable<Void>> batches = new ArrayList<>(internalAlgorithms.size());
        for (MutableGeneticAlgorithm internalAlgorithm : internalAlgorithms) {
            batches.add(() -> {
                internalAlgorithm.optimize();
                return null;
            });
        }

        try {
            executorService.invokeAll(batches);
        } catch (InterruptedException e) {
            throw new RuntimeException("Exception thrown invoking GA jobs.", e);
        }

        for (MutableGeneticAlgorithm internalAlgorithm : internalAlgorithms) {
            if (internalAlgorithm.getBestFitness() > bestFitness) {
                bestFitness = internalAlgorithm.getBestFitness();
                bestConfiguration = internalAlgorithm.getBestConfiguration();
            }
            newGeneration.addAll(internalAlgorithm.getPopulation());
        }

        population = newGeneration;
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        executorService.shutdown();
    }

    private static class MutableGeneticAlgorithm extends GeneticAlgorithm {
        public MutableGeneticAlgorithm(int populationSize, double mutationRate) {
            super(populationSize, mutationRate);
        }

        public List<Chromosome> getPopulation() {
            return population;
        }

        public void setSelection(Selection selection) {
            this.selection = selection;
        }
    }

    private static class SelectionAdapter implements Selection {
        private final Selection delegate;

        private SelectionAdapter(Selection delegate) {
            this.delegate = delegate;
        }

        @Override
        public void init(List<Chromosome> population) {
            // do nothing
        }

        @Override
        public Chromosome select() {
            return this.delegate.select();
        }
    }
}
