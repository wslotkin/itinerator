package itinerator.config;

public class EvaluationConfig {
    private final double costPenalty;
    private final double incorrectMealPenalty;
    private final double areaHoppingPenalty;
    private final double areaHoppingThreshold;
    private final double incorrectSleepPenalty;
    private final double travelTimePenalty;
    private final double invalidHoursPenalty;
    private final double missingSleepMinutesPenalty;

    public EvaluationConfig(double costPenalty,
                            double incorrectMealPenalty,
                            double areaHoppingPenalty,
                            double areaHoppingThreshold,
                            double incorrectSleepPenalty,
                            double travelTimePenalty,
                            double invalidHoursPenalty,
                            double missingSleepMinutesPenalty) {
        this.costPenalty = costPenalty;
        this.incorrectMealPenalty = incorrectMealPenalty;
        this.areaHoppingPenalty = areaHoppingPenalty;
        this.areaHoppingThreshold = areaHoppingThreshold;
        this.incorrectSleepPenalty = incorrectSleepPenalty;
        this.travelTimePenalty = travelTimePenalty;
        this.invalidHoursPenalty = invalidHoursPenalty;
        this.missingSleepMinutesPenalty = missingSleepMinutesPenalty;
    }

    public double getCostPenalty() {
        return costPenalty;
    }

    public double getIncorrectMealPenalty() {
        return incorrectMealPenalty;
    }

    public double getAreaHoppingPenalty() {
        return areaHoppingPenalty;
    }

    public double getAreaHoppingThreshold() {
        return areaHoppingThreshold;
    }

    public double getIncorrectSleepPenalty() {
        return incorrectSleepPenalty;
    }

    public double getTravelTimePenalty() {
        return travelTimePenalty;
    }

    public double getInvalidHoursPenalty() {
        return invalidHoursPenalty;
    }

    public double getMissingSleepMinutesPenalty() {
        return missingSleepMinutesPenalty;
    }

    public Builder toBuilder() {
        return new Builder()
                .setCostPenalty(costPenalty)
                .setIncorrectMealPenalty(incorrectMealPenalty)
                .setAreaHoppingPenalty(areaHoppingPenalty)
                .setAreaHoppingThreshold(areaHoppingThreshold)
                .setIncorrectSleepPenalty(incorrectSleepPenalty)
                .setTravelTimePenalty(travelTimePenalty)
                .setInvalidHoursPenalty(invalidHoursPenalty)
                .setMissingSleepMinutesPenalty(missingSleepMinutesPenalty);
    }

    public static class Builder {
        private double costPenalty = -10.0;
        private double incorrectMealPenalty = -20.0;
        private double areaHoppingPenalty = -50.0;
        private double areaHoppingThreshold = 15.0;
        private double incorrectSleepPenalty = -100.0;
        private double travelTimePenalty = -20.0;
        private double invalidHoursPenalty = -75.0;
        private double missingSleepMinutesPenalty = -5.0;

        public Builder setCostPenalty(double costPenalty) {
            this.costPenalty = costPenalty;
            return this;
        }

        public Builder setIncorrectMealPenalty(double incorrectMealPenalty) {
            this.incorrectMealPenalty = incorrectMealPenalty;
            return this;
        }

        public Builder setAreaHoppingPenalty(double areaHoppingPenalty) {
            this.areaHoppingPenalty = areaHoppingPenalty;
            return this;
        }

        public Builder setAreaHoppingThreshold(double areaHoppingThreshold) {
            this.areaHoppingThreshold = areaHoppingThreshold;
            return this;
        }

        public Builder setIncorrectSleepPenalty(double incorrectSleepPenalty) {
            this.incorrectSleepPenalty = incorrectSleepPenalty;
            return this;
        }

        public Builder setTravelTimePenalty(double travelTimePenalty) {
            this.travelTimePenalty = travelTimePenalty;
            return this;
        }

        public Builder setInvalidHoursPenalty(double invalidHoursPenalty) {
            this.invalidHoursPenalty = invalidHoursPenalty;
            return this;
        }

        public Builder setMissingSleepMinutesPenalty(double missingSleepMinutesPenalty) {
            this.missingSleepMinutesPenalty = missingSleepMinutesPenalty;
            return this;
        }

        public EvaluationConfig build() {
            return new EvaluationConfig(costPenalty,
                    incorrectMealPenalty,
                    areaHoppingPenalty,
                    areaHoppingThreshold,
                    incorrectSleepPenalty,
                    travelTimePenalty,
                    invalidHoursPenalty,
                    missingSleepMinutesPenalty);
        }
    }
}
