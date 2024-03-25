
package cycling;

import java.time.LocalTime;

public class Result {
    // Declare private variables for the rider ID, stage ID, checkpoint times, and points
    private int riderId;
    private int stageId;
    private LocalTime[] checkpointTimes;
    private int points;
    
    // Constructor for the Result class
    public Result(int riderId, int stageId, LocalTime [] checkpointTimes, int points) {
        // Initialize the rider ID, stage ID, checkpoint times, and points
        this.riderId = riderId;
        this.stageId = stageId;
        this.checkpointTimes = checkpointTimes;
        this.points = points;
    }

    // Getter method for the rider ID
    public int getRiderId() {
        return riderId;
    }

    // Getter method for the stage ID
    public int getStageId() {
        return stageId;
    }

    // Getter method for the checkpoint times
    public LocalTime[] getCheckpointTimes() {
        return checkpointTimes;
    }

    // Getter method for the points
    public int getPoints() {
        return points;
    }
}