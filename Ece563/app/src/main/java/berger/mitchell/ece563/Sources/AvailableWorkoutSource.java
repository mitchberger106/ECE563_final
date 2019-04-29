package berger.mitchell.ece563.Sources;

public class AvailableWorkoutSource {
    private String name;
    private String bodyType;

    public AvailableWorkoutSource() {
    }

    public AvailableWorkoutSource(String name, String bodyType) {
        this.name = name;
        this.bodyType = bodyType;
    }

    public String getName() {
        return name;
    }

    public String getBodyType() {
        return bodyType;
    }
}
