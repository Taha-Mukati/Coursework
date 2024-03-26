package cycling;

import java.time.LocalTime;

public class Checkpoint {
    // Declare private variables for the checkpoint ID, location, type, and time
    private int checkpointId;
    private double location;
    private CheckpointType type;
    private LocalTime time;

    // Constructor for the Checkpoint class
    public Checkpoint(int checkpointId, double location, CheckpointType type) {
        this.checkpointId = checkpointId;
        this.location = location;
        this.type = type;
    }
    
    // Getter methods for the checkpoint ID, location, type, and time
    public int getCheckpointId() {
        return checkpointId; 
    }   

    public double getLocation() {
        return location;
    }

    public CheckpointType getType() {
        return type;
    }

    public LocalTime getTime() {
        return time;
    }
    
}