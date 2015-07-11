package itinerator.config;

public class OptimizationConfig {
    private final ItineratorConfig itineratorConfig;
    private final GeneticAlgorithmConfig geneticAlgorithmConfig;
    private final EvaluationConfig evaluationConfig;

    public OptimizationConfig(ItineratorConfig itineratorConfig,
                              GeneticAlgorithmConfig geneticAlgorithmConfig,
                              EvaluationConfig evaluationConfig) {
        this.itineratorConfig = itineratorConfig;
        this.geneticAlgorithmConfig = geneticAlgorithmConfig;
        this.evaluationConfig = evaluationConfig;
    }

    public ItineratorConfig getItineratorConfig() {
        return itineratorConfig;
    }

    public GeneticAlgorithmConfig getGeneticAlgorithmConfig() {
        return geneticAlgorithmConfig;
    }

    public EvaluationConfig getEvaluationConfig() {
        return evaluationConfig;
    }

    public Builder toBuilder() {
        return new Builder()
                .setItineratorConfig(itineratorConfig)
                .setGeneticAlgorithmConfig(geneticAlgorithmConfig)
                .setEvaluationConfig(evaluationConfig);
    }

    public static class Builder {
        private ItineratorConfig itineratorConfig = new ItineratorConfig.Builder().build();
        private GeneticAlgorithmConfig geneticAlgorithmConfig = new GeneticAlgorithmConfig.Builder().build();
        private EvaluationConfig evaluationConfig = new EvaluationConfig.Builder().build();

        public Builder setItineratorConfig(ItineratorConfig itineratorConfig) {
            this.itineratorConfig = itineratorConfig;
            return this;
        }

        public Builder setGeneticAlgorithmConfig(GeneticAlgorithmConfig geneticAlgorithmConfig) {
            this.geneticAlgorithmConfig = geneticAlgorithmConfig;
            return this;
        }

        public Builder setEvaluationConfig(EvaluationConfig evaluationConfig) {
            this.evaluationConfig = evaluationConfig;
            return this;
        }

        public OptimizationConfig build() {
            return new OptimizationConfig(itineratorConfig,
                    geneticAlgorithmConfig,
                    evaluationConfig);
        }
    }
}
