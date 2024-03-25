package cycling;

public class Climb extends Checkpoint {
    // Declare private variables for the stage ID, average gradient, and length
    private int stageId;
    private double averageGradient;
    private double length;

    // Constructor for the ClimbCheckpoint class
    public Climb(int id, int stageId, double location, CheckpointType type, double averageGradient, double length) {
        // Call the superclass constructor and initialize the stage ID, average gradient, and length
        super(id, location, type);
        this.stageId = stageId;
        this.averageGradient = averageGradient;
        this.length = length;
    }

    // Getter method for the stage ID
    public int getStageId() {
        return stageId;
    }

    // Setter method for the stage ID
    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    // Getter method for the average gradient
    public double getAverageGradient() {
        return averageGradient;
    }

    // Setter method for the average gradient
    public void setAverageGradient(double averageGradient) {
        this.averageGradient = averageGradient;
    }

    // Getter method for the length
    public double getLength() {
        return length;
    }

    // Setter method for the length
    public void setLength(double length) {
        this.length = length;
    }
}