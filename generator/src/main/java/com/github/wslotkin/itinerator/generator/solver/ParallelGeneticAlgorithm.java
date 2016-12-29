package com.github.wslotkin.itinerator.generator.solver;

import com.google.common.collect.ForwardingList;
import cz.cvut.felk.cig.jcop.algorithm.geneticalgorithm.Chromosome;
import cz.cvut.felk.cig.jcop.algorithm.geneticalgorithm.GeneticAlgorithm;
import cz.cvut.felk.cig.jcop.problem.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

class ParallelGeneticAlgorithm extends GeneticAlgorithm {
    private final List<Integer> list;

    public ParallelGeneticAlgorithm(int populationSize, double mutationRate) {
        super(populationSize, mutationRate);
        list = IntStream.range(0, populationSize / 2).boxed().collect(Collectors.toList());
    }

    @Override
    public void optimize() {
        selection.init(population);

        PopulationAndFitness populationAndFitness = list.parallelStream()
                .flatMap(i -> createChildren())
                .peek(chromosome -> chromosome.setFitness(fitness.getValue(chromosome.getConfiguration())))
                .collect(toCollection(() -> new PopulationAndFitness(bestConfiguration, bestFitness, populationSize)));

        population = populationAndFitness.population;
        bestFitness = populationAndFitness.bestFitness;
        bestConfiguration = populationAndFitness.bestConfiguration;
    }

    private Stream<Chromosome> createChildren() {
        return reproduction.reproduce(selection.select(), selection.select(), mutation).stream();
    }

    private static class PopulationAndFitness extends ForwardingList<Chromosome> {

        private final List<Chromosome> population;
        private Configuration bestConfiguration;
        private double bestFitness;

        private PopulationAndFitness(Configuration bestConfiguration, double bestFitness, int populationSize) {
            this.bestConfiguration = bestConfiguration;
            this.bestFitness = bestFitness;
            population = new ArrayList<>(populationSize);
        }

        @Override
        public boolean add(Chromosome element) {
            updateBest(element);
            return super.add(element);
        }

        @Override
        protected List<Chromosome> delegate() {
            return population;
        }

        private synchronized void updateBest(Chromosome element) {
            if (element.getFitness() > bestFitness) {
                bestFitness = element.getFitness();
                bestConfiguration = element.getConfiguration();
            }
        }
    }
}
