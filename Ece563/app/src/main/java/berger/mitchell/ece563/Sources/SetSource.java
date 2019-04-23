package berger.mitchell.ece563.Sources;

public class SetSource {
    private String weight;
    private String reps;

    public SetSource() {
    }

    public SetSource(String weight, String reps) {
        this.weight = weight;
        this.reps = reps;
    }

    public String getWeight() {
        return weight;
    }

    public String getReps() {
        return reps;
    }
}
