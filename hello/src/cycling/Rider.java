package cycling;

public class Rider {
    // Declare private variables for the rider name, ID, year of birth, and team ID
    private String riderName;
    private int riderId;
    private int teamId;
    private Integer riderYob;

    // Constructor for the Rider class
    public Rider (String riderName, int riderId, int riderYob, int teamId) {
        this.riderName = riderName;
        this.riderId = riderId;
        this.riderYob = riderYob;
        this.teamId = teamId;
    }
    // Getter methods for the rider name, ID, year of birth, and team ID
    public String getRiderName() {
        return riderName;
    }
    public int getRiderid() {
        return riderId;
    }
    public int getTeamid() {
        return teamId;
    }
    public int getRideryob() {
        return riderYob;
    }
}