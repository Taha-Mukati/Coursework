package cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Stage {

    // Declare private variables for the stage name, ID, length, description, start time, results, type, checkpoints, and state
    private String stageName;
    private int stageId;
    private double length;
    private String stageDescription;
    private LocalDateTime startTime;
    private ArrayList<Result> results;
    private StageType stageType;
    private ArrayList<Checkpoint> checkpoints;
    private StageState stageState;

   
    
    // Constructor for the Stage class with checkpoints and results
    public Stage (String stageName, int stageId, double length, String stageDescription, LocalDateTime startTime, StageType stageType, ArrayList<Checkpoint> checkpoints, StageState stageState, ArrayList<Result> results) {
        this.stageId = stageId;
        this.stageName = stageName;
        this.length = length;
        this.stageDescription = stageDescription;
        this.startTime = startTime;
        this.stageType = stageType;
        this.checkpoints = checkpoints;
        this.stageState = stageState;
        this.results = results;
    }

    // Getter methods for the stage name, ID, length, description, start time, type, checkpoints, state, and results
    public String getStageName(){
        return stageName;
    }
    public int getStageId(){
        return stageId;
    }
    public double getLength() {
        return length;
    }
    public String getStageDescription(){
        return stageDescription;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public StageType getStageType(){
        return stageType;
    }
    public ArrayList<Checkpoint> getCheckpoints(){
        return checkpoints;
    }
    public StageState getStageState(){
        return stageState;
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
    public void setStageState(StageState stageState){
        this.stageState = stageState;
    }
}