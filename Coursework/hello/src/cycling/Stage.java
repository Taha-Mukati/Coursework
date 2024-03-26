package cycling;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Stage {

    // Declare private variables for the stage name, ID, length, description, start time, results, type, checkpoints, and state
    private String stageName;
    private int stageId;
    private double stageLength;
    private String stageDescription;
    private LocalDateTime startTime;
    private ArrayList<Result> results;
    private StageType stageType;
    private ArrayList<Checkpoint> checkpoints;
    private StageState stageState;

   
    
    // Constructor for the Stage class with checkpoints and results
    public Stage (String stageName, int stageId, double stageLength, String stageDescription, LocalDateTime startTime, StageType stageType, ArrayList<Checkpoint> checkpoints, StageState stageState, ArrayList<Result> results) {
        this.stageId = stageId;
        this.stageName = stageName;
        this.stageLength = stageLength;
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
    public double getStageLength() {
        return stageLength;
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

    public void addCheckpoint(int checkpointId, double location, CheckpointType type){
        Checkpoint checkpoint = new Checkpoint(checkpointId, location, type);
        checkpoints.add(checkpoint);
    }
    public void addClimbCheckpoint(int id, int stageId, double location, CheckpointType type, double averageGradient, double length){
        Checkpoint checkpoint = new Climb (id, stageId, location, type, averageGradient, length);
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
    public void addResult(int riderId, int stageId, LocalTime[] checkpointTimes, int points){
        Result result = new Result(riderId, stageId, checkpointTimes, points);
        results.add(result);
    }
}