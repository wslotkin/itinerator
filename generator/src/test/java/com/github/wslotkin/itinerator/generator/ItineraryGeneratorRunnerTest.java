package com.github.wslotkin.itinerator.generator;

import com.github.wslotkin.itinerator.generator.config.ImmutableOptimizationConfig;
import com.github.wslotkin.itinerator.generator.data.DataFileActivityProvider;
import com.github.wslotkin.itinerator.generator.datamodel.SolverResult;
import org.junit.Test;

import static com.github.wslotkin.itinerator.generator.ItineraryGeneratorRunner.createOptimizer;
import static com.github.wslotkin.itinerator.generator.main.FileBasedItineraryGeneratorRunner.BEIJING_DATA;
import static org.junit.Assert.assertNotNull;

public class ItineraryGeneratorRunnerTest {
    @Test
    public void canRunOptimization() {
        ItineraryGeneratorRunner optimizer = createOptimizer(ImmutableOptimizationConfig.builder().build(),
                new DataFileActivityProvider(BEIJING_DATA));
        SolverResult optimizerResult = optimizer.run();
        assertNotNull(optimizerResult);
    }
}