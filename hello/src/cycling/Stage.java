package cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Stage {

    // Declare private variables for the stage name, ID, length, description, start time, results, type, checkpoints, and state
    private String name;
    private int id;
    private double length;
    private String description;
    private LocalDateTime startTime;
    private ArrayList<Result> results;
    private StageType type;
    private ArrayList<Checkpoint> checkpoints;
    private StageState state;

    // Constructor for the Stage class without checkpoints and results
    public Stage (String name, int id, double length, String description, LocalDateTime startTime, StageType type, StageState state) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.description = description;
        this.startTime = startTime;
        this.type = type;
        this.state = state;
    }
    
    // Constructor for the Stage class with checkpoints and results
    public Stage (String name, int id, double length, String description, LocalDateTime startTime, StageType type, ArrayList<Checkpoint> checkpoints, StageState state, ArrayList<Result> results) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.description = description;
        this.startTime = startTime;
        this.type = type;
        this.checkpoints = checkpoints;
        this.state = state;
        this.results = results;
    }

    // Getter methods for the stage name, ID, length, description, start time, type, checkpoints, state, and results
    public String getStageName(){
        return name;
    }
    public int getStageId(){
        return id;
    }
    public double getStageLength() {
        return length;
    }
    public String getStageDescription(){
        return description;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public StageType getStageType(){
        return type;
    }
    public ArrayList<Checkpoint> getCheckpoints(){
        return checkpoints;
    }
    public StageState getStageState(){
        return state;
    }
    public ArrayList<Result> getResults(){
        return results;
    }

    // Method to add a checkpoint to the stage
    public void addCheckpoint(Checkpoint checkpoint){
        checkpoints.add(checkpoint);
    }

    // Method to remove a checkpoint from the stage
    public void removeCheckpoint(Checkpoint checkpoint){
        checkpoints.remove(checkpoint);
    }

    // Setter method for the stage state
    public void setStageState(StageState state){
        this.state = state;
    }
}