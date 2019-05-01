package berger.mitchell.ece563.Sources;

public class MaxSource {
    private String name;
    private int max;

    public MaxSource(String name, int max) {
        this.name = name;
        this.max = max;
    }
    public int getMax(){
        return max;
    }
    public String getName(){
        return name;
    }

}
