package berger.mitchell.ece563.Sources;

public class DailyWorkoutSource {
    private String name;
    private String reps;
    private String weight;

    public DailyWorkoutSource() {
    }

    public DailyWorkoutSource(String name, String reps, String weight) {
        this.name = name;
        this.reps = reps;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }
    public String getReps() {
        return reps;
    }
    public String getWeight() {
        return weight;
    }
}
