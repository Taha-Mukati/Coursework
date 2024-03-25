package cycling;

import java.util.ArrayList;

public class Race {

    // Declare private variables for the teams, race name, ID, description, stages, and total length
    private ArrayList <Team> teams;
    private String name;
    private int id;
    private String description;
    private ArrayList<Stage> stages = new ArrayList<>();
    private double totalLength;

    // Constructor for the Race class with teams
    public Race(String name, ArrayList<Team> teams, int id, String description){
        this.name = name;
        this.teams = teams;
        this.id = id;
        this.description = description;
    }
    
    // Getter methods for the race name, ID, description, stages, and total length
    public String getRaceName() {
        return name;
    }
    
    public int getRaceId() {
        return id;
    }
    
    public String getRaceDescription() {
        return description;
    }
    
    public ArrayList<Stage> getStages() {
        return stages;
    }
    
    public double getTotalLength() {
        return totalLength;
    }

    // Setter method for the race ID
    public void setRaceId(int id) {
        this.id = id;
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