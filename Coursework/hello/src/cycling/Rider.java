package cycling;

public class Rider {
    // Declare private variables for the rider name, ID, year of birth, and team ID
    private String riderName;
    private int riderId;
    private Integer riderYob;

    // Constructor for the Rider class
    public Rider (String riderName, int riderId, int riderYob) {
        this.riderName = riderName;
        this.riderId = riderId;
        this.riderYob = riderYob;
    }
    // Getter methods for the rider name, ID ans year of birth
    public String getRiderName() {
        return riderName;
    }
    public int getRiderId() {
        return riderId;
    }
    
    public int getRiderYob() {
        return riderYob;
    }
}