package cycling;

public class Rider {
    private String name;
    private int riderId;
    private int teamId;
    private Integer riderYob;

    public Rider (String name, int riderId, int riderYob, int teamId) {
        this.name = name;
        this.riderId = riderId;
        this.riderYob = riderYob;
        this.teamId = teamId;
    }
    
    public String getName() {
        return name;
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