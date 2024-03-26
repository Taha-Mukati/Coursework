package cycling;

import java.io.Serializable;
import java.util.ArrayList;

public class Race implements Serializable {

    // Declare private variables for the teams, race name, ID, description, stages, and total length
    private ArrayList <Team> teams;
    private String raceName;
    private int raceId;
    private String raceDescription;
    private ArrayList<Stage> stages = new ArrayList<>();
    private double totalLength;

    // Constructor for the Race class with teams
    public Race(String raceName, int raceId, String raceDescription, double totalLength, ArrayList<Team> teams, ArrayList<Stage> stages) {
        this.raceName = raceName;
        this.teams = teams;
        this.raceId = raceId;
        this.raceDescription = raceDescription;
        this.totalLength = totalLength;
        this.stages = stages;
    }
    
    // Getter methods for the race name, ID, description, stages, and total length
    public String getRaceName() {
        return raceName;
    }
    
    public int getRaceId() {
        return raceId;
    }
    
    public String getRaceDescription() {
        return raceDescription;
    }
    
    public ArrayList<Stage> getStages() {
        return stages;
    }
    
    public double getTotalLength() {
        return totalLength;
    }
    
    // Method to add a stage to the race
    public void addRaceStage(Stage stage) {
        stages.add(stage);
    }
    
    // Method to remove a stage from the race
    public void removeStage(Stage stage) {
        stages.remove(stage);
    }
    
    // Method to set the total length of the race
    public void setTotalLength() {
        for (int i = 0; i < stages.size(); i++) {
            totalLength += stages.get(i).getStageLength();
        }
    }
}