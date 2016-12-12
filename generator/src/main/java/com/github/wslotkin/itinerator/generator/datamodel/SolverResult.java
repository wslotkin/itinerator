package com.github.wslotkin.itinerator.generator.datamodel;

import cz.cvut.felk.cig.jcop.problem.Configuration;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

@Immutable
public interface SolverResult {
    @Parameter
    Itinerary getItinerary();

    @Parameter
    Configuration getConfiguration();

    @Parameter
    double getScore();

    @Parameter
    long getDuration();
}
