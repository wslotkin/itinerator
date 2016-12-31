package com.github.wslotkin.itinerator.generator.experimenter;

import com.github.wslotkin.itinerator.generator.datamodel.Itinerary;
import com.github.wslotkin.itinerator.generator.solver.ItineraryFitness;
import com.google.common.collect.TreeMultiset;

import java.util.Iterator;

public class ItineraryExperiment implements Iterable<ItineraryExperiment.Entry> {
    private final ItineraryFitness fitness;

    // We use a multiset because it's possible to have equal fitness values and that shouldn't tank the experiment.
    private TreeMultiset<Entry> itineraries;
    private double normalizationFactor = 0.0;

    public ItineraryExperiment(ItineraryFitness fitness) {
        this.fitness = fitness;
    }

    public boolean add(Itinerary itinerary) {
        double rawFitness = fitness.getValue(itinerary);
        if (Math.abs(rawFitness) > normalizationFactor) {
            normalizationFactor = Math.abs(rawFitness);
        }

        return itineraries.add(new Entry(itinerary, rawFitness));
    }

    @Override
    public Iterator<Entry> iterator() {
        Iterator<Entry> baseIterator = itineraries.iterator();
        return new EntryIterator(normalizationFactor, baseIterator);
    }

    public class Entry implements Comparable<Entry> {
        public final Itinerary itinerary;
        public final double fitness;

        private Entry(Itinerary itinerary, double fitness) {
            this.itinerary = itinerary;
            this.fitness = fitness;
        }

        @Override
        public int compareTo(Entry other) {
            // We deliberately switch the order here so that the "natural ordering" of Entries is descending, not
            // ascending.
            return Double.compare(other.fitness, this.fitness);
        }
    }

    private class EntryIterator implements Iterator<Entry> {
        private final double normalizationFactor;
        private Iterator<Entry> baseIterator;

        private EntryIterator(double normalizationFactor, Iterator<Entry> baseIterator) {
            this.normalizationFactor = normalizationFactor;
            this.baseIterator = baseIterator;
        }

        @Override
        public boolean hasNext() {
            return baseIterator.hasNext();
        }

        @Override
        public Entry next() {
            Entry next = baseIterator.next();
            return new Entry(next.itinerary, next.fitness / normalizationFactor);
        }
    }
}