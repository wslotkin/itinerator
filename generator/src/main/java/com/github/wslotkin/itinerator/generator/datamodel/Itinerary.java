package com.github.wslotkin.itinerator.generator.datamodel;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import java.util.List;

@Immutable
public interface Itinerary {
    @Parameter
    List<Event> getEvents();
}
