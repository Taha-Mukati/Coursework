package cycling;

import java.util.ArrayList;


public class Team{
    // Declare private variables for the team name, riders, ID, and description
    private ArrayList<Rider> riders;
    private String teamName;
    private Integer teamId;
    private String teamDescription;
    
    // Constructor for the Team class with team name, riders, ID, and description
    public Team(String teamName, ArrayList<Rider> riders, int teamId, String teamDescription) {
        this.teamName = teamName;
        this.riders = riders;
        this.teamId = teamId;
        this.teamDescription = teamDescription;
    }

    // Getter methods for the team name, riders, ID, and description
    public ArrayList<Rider> getRiders() {
        return riders;
    }
    public String getTeamName() {
        return teamName;
    }
   
    public int getTeamId() {
        return teamId;
    }
    public String getTeamDescription(){
        return teamDescription;
    }
}
