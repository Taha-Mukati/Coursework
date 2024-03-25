package cycling;

import javax.naming.InvalidNameException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.util.UUID;
import java.util.stream.Collectors;

import cycling.exceptions.IllegalNameException;
import cycling.exceptions.InvalidNameException;
import cycling.exceptions.IDNotRecognisedException;
import cycling.exceptions.InvalidLocationException;
import cycling.exceptions.InvalidStageStateException;
import cycling.exceptions.InvalidStageTypeException;
import java.util.UUID;
import java.time.LocalTime;


public class CyclingPortalImpl implements CyclingPortal {

    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Race> races = new ArrayList<>();

    
    Integer teamCounter = 0;
    Integer raceCounter = 0;


    /**
     * Get the races currently created in the platform.
     *
     * @return An array of race IDs in the system or an empty array if none exists.
     */
    public int [] getRaceIds() {
       int[] raceIds;
        
        // return an empty array if races is null or empty
        if (races == null || races.isEmpty()) {
            return new int[0];
        }
        //assigning raceIds to the elements of an array
        raceIds = new int[races.size()];
        for (int i=0; i<races.size(); i++){
           raceIds[i] = races.get(i).getRaceId();
        }
        return raceIds;
    }

    /** 
     * The method creates a staged race in the platform with the given name and
     * description.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param name        Race's name.
     * @param description Race's description (can be null).
     * @return the unique ID of the created race.
     * @throws IllegalNameException If the name already exists in the platform.
     * @throws InvalidNameException If the name is null, empty, has more than 30
     *                              characters, or has white spaces.
     */
    @Override
    public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {

        Race race;
        int raceId;
        //checking if duplicate names exist
        for(int i = 0; i<races.size(); i++) {
            if (name.equals(races.get(i).getName())) {
                throw new IllegalNameException("Name already exists");
            }
        }
        //checking if name is valid
        if (name == null || name.isEmpty() || name.length()>30 || name.contains(" ") ) {
            throw new InvalidNameException("Invalid name");
        }
        //creating a unique ID for the races
        UUID id = UUID.randomUUID();
        raceId = (int) id.getMostSignificantBits();
        race = new Race(name, description, raceId);

        return raceId;
    }

    /**
     * Get the details from a race.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param raceId The ID of the race being queried.
     * @return Any formatted string containing the race ID, name, description, the
     * number of stages, and the total length (i.e., the sum of all stages'
     * length).
     * @throws IDNotRecognisedException If the ID does not match to any race in the
     *                                  system.
     */
    @Override
    public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
    
    //init variable
    boolean found = false;
    int i=0;
    String details = "";

    //looking for raceIDs in races
    while (!found && i<races.size()) {
        if (raceId == races.get().getRaceId) {
            found = true;
            //converting Race toString
            details = "Race Details = (Race ID  = " + races.get(i).getRaceId() + ", Name = " + races.get(i).getName() + 
            ", Description = " + races.get(i).getDescription() + ", Number of Stages = " 
            + getNumberOfStages(raceId) + ", Total Length = " + races.get(i).getTotalLength() +")"; 
            } i++;
        }
        if (!found) {
            throw new IDNotRecognisedException("ID not recognised");
        } else {
        }
        return details;
    }

/**
	 * The method removes the race and all its related information, i.e., stages,
	 * checkpoints, and results.
	 * <p>
	 * The state of this MiniCyclingPortal must be unchanged if any
	 * exceptions are thrown.
	 * 
	 * @param raceId The ID of the race to be removed.
	 * @throws IDNotRecognisedException If the ID does not match to any race in the
	 *                                  system.
	 */
    @Override
    public void removeRaceById(int raceId) throws IDNotRecognisedException {
        // Initialize a boolean variable to track if the race is found
        boolean found = false;
        
        // Loop through the list of races
        for (int i = 0; i < races.size(); i++) {
            // If the current race's ID matches the given ID
            if (races.get(i).getRaceId() == raceId) {
                // Remove the race from the list
                races.remove(i);
                // Set found to true and break the loop
                found = true;
                break;
            }
        }
        // If the race was not found in the list
        if (!found) {
            // Throw an exception
            throw new IDNotRecognisedException("ID not recognised");
        }
    }

    /**(Taha)
     * The method queries the number of stages created for a race.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param raceId The ID of the race being queried.
     * @return The number of stages created for the race.
     * @throws IDNotRecognisedException If the ID does not match to any race in the
     *                                  system.
     */
    @Override
    public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
        //init variables
        boolean found = false;
        int i=0;
        int numberOfStages;
        //finding RaceID in races and getting number of stages
        while (!found && i<races.size()) {
            if (races.get(i).getRaceId() == raceId) {
                found = true;
                numberOfStages = races.get(i).getStages().size();
        }i++;
        if (!found) {
            // throws IDnotRecognisedException if ID not found
            throw new IDNotRecognisedException("ID not recognised");
        } else {
            return numberOfStages;
        }
    }

    /**(Suraj)
     * Creates a new stage and adds it to the race.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param raceId      The race which the stage will be added to.
     * @param stageName   An identifier name for the stage.
     * @param description A descriptive text for the stage.
     * @param length      Stage length in kilometres.
     * @param startTime   The date and time in which the stage will be raced. It
     *                    cannot be null.
     * @param type        The type of the stage. This is used to determine the
     *                    amount of points given to the winner.
     * @return the unique ID of the stage.
     * @throws IDNotRecognisedException If the ID does not match to any race in the
     *                                  system.
     * @throws IllegalNameException     If the name already exists in the platform.
     * @throws InvalidNameException     If the name is null, empty, has more than 30
     *                                  characters, or has white spaces.
     * @throws InvalidLengthException   If the length is less than 5km.
     */
@Override
public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime, StageType type) throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
    // Validate inputs
    if (stageName == null || stageName.isEmpty() || stageName.length() > 30 || stageName.contains(" ")) {
        throw new InvalidNameException("Invalid stage name");
    }
    if (length < 5) {
        throw new InvalidLengthException("Invalid stage length");
    }
    if (startTime == null) {
        startTime = LocalDateTime.MIN; // Set startTime to minimum value if it's null
    }

    // Find the race
    Race foundRace = null;
    // Loop through the list of races
    for (Race race : races) {
        // Find the race with the given raceId
        if (race.getRaceId() == raceId) {
            foundRace = race;
            break;
        }
    }
    // Throw an exception if the raceId is not found
    if (foundRace == null) {
        throw new IDNotRecognisedException("Race ID not recognised");
    }

    // Check if stage name already exists
    for (Stage stage : foundRace.getStages()) {
        if (stage.getStageName().equals(stageName)) {
            throw new IllegalNameException("Stage name already exists");
        }
    }
    // Create and add the stageID & add stage to race.
    UUID id = UUID.randomUUID();
    int stageId = (int) id.getMostSignificantBits();
    // Initialising lists for Results and Checkpoints
    ArrayList<Result> results = new ArrayList<>();
    ArrayList<Checkpoint> checkpoints = new ArrayList<>();
    StageState stageState = StageState.WAITING_FOR_RESULTS; // Declare a StageState variable
    Stage stage = new Stage(stageName, stageId, length, description, startTime, type, checkpoints, stageState, results); 
    foundRace.addRaceStage(stage);

    return stage.getStageId();
}

    /**(Taha)
     * Retrieves the list of stage IDs of a race.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param raceId The ID of the race being queried.
     * @return An array of stage IDs ordered (from first to last) by their sequence in the
     * race or an empty array if none exists.
     * @throws IDNotRecognisedException If the ID does not match to any race in the
     *                                  system.
     */
    @Override
    public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
        //init variables
        int[] stageIds;
        int i=0;
        boolean found = false;
        //finding race using raceID
        while(!found && i<races.size()) {
            if (races.get(i).getRaceId() == raceId) {
                found = true;
                stageIds = new int[races.get(i).getStages().size()];
                //adding stageIDs to array
                for (int j=0; j<races.get(i).getStages().size(); j++) {
                    stageIds[j] = races.get(i).getStages().get(j).getStageId();
                    }
                }i++;      
            }
    
        if (!found) {
            //throws IDNotRecognisedException if race ID not found
            throw new IDNotRecognisedException("ID not recognised");
        } else {
            return stageIds;
        }
    /**(Suraj)
     * Gets the length of a stage in a race, in kilometres.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param stageId The ID of the stage being queried.
     * @return The stage's length.
     * @throws IDNotRecognisedException If the ID does not match to any stage in the
     *                                  system.
     */
    @Override
    public double getStageLength(int stageId) throws IDNotRecognisedException {
        // Loop through each race in the races list
        for (Race race : races) {
            // For each race, loop through its stages
            for (Stage stage : race.getStages()) {
                // If the current stage's ID matches the given ID
                if (stage.getStageId() == stageId) {
                    // Return the length of the stage
                    return stage.getStageLength();
                }
            }
        }
        // If no stage with the given ID is found, throw an exception
        throw new IDNotRecognisedException("Stage ID not recognised");
    }

    /** (Taha)
     * Removes a stage and all its related data, i.e., checkpoints and results.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
    /**
     * exceptions are thrown.
     *
     * @param stageId The ID of the stage being removed.
     * @throws IDNotRecognisedException If the ID does not match to any stage in the
     *                                  system.
     */
    @Override
    public void removeStageById(int stageId) throws IDNotRecognisedException {
        //init variables
        int i=0;
        int j=0;
        boolean found = false;
        //checking if stageID exists in stages
        while (!found && i<races.size()) {
            while(!found && j<races.get(i).getStages().size()) {
                stages = races.get(i).getStages();
                if (stages.get(j).getStageId() == stageId) {
                    //removing stageID
                    stages.remove(j);
                }
                j++;
            }
            i++;
        }

        if (!found) {
            //throws IDNotRecognised when stageID not found
            throw new IDNotRecognisedException("ID not recognised");
        }
    }

    /**(Suraj)
     * Adds a climb checkpoint to a stage.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param stageId         The ID of the stage to which the climb checkpoint is
     *                        being added.
     * @param location        The kilometre location where the climb finishes within
     *                        the stage.
     * @param type            The category of the climb - {@link CheckpointType#C4},
     *                        {@link CheckpointType#C3}, {@link CheckpointType#C2},
     *                        {@link CheckpointType#C1}, or {@link CheckpointType#HC}.
     * @param averageGradient The average gradient for the climb.
     * @param length          The length of the climb in kilometre.
     * @return The ID of the checkpoint created.
     * @throws IDNotRecognisedException   If the ID does not match to any stage in
     *                                    the system.
     * @throws InvalidLocationException   If the location is out of bounds of the
     *                                    stage length.
     * @throws InvalidStageStateException If the stage is "waiting for results".
     * @throws InvalidStageTypeException  Time-trial stages cannot contain any
     *                                    checkpoint.
     */
    @Override
    public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient, Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
        // Find the stage with the given stageId
        Stage stage = null;
        for (Race race : races) {
            for (Stage s : race.getStages()) {
                if (s.getStageId() == stageId) {
                    stage = s;
                    break;
                }
            }
        }
        // If no stage with the given ID is found, throw an exception
        if (stage == null) {
            throw new IDNotRecognisedException("Stage ID not recognised");
        }

        // Validate the location input. It should be between 0 and the stage's length
        if (location < 0 || location > stage.getStageLength()) {
            throw new InvalidLocationException("Invalid location");
        }
        // Validate the stage's state. It should not be in the 'waiting for results' state
        if (stage.getStageState().equals(StageState.WAITING_FOR_RESULTS)) {
            throw new InvalidStageStateException("Invalid stage state");
        }
        // Validate the stage's type. Time-trial stages cannot contain any checkpoint
        if (stage.getStageType().equals(StageType.TIME_TRIAL)) {
            throw new InvalidStageTypeException("Time-trial stages cannot contain any checkpoint");
        }

        // Generate a unique ID for the checkpoint
        UUID id = UUID.randomUUID();
        int checkpointId = (int) id.getMostSignificantBits();
        // Create a new climb
        Climb checkpoint = new Climb(checkpointId, stageId, location, type, averageGradient, length);
        // Add the checkpoint to the stage
        stage.addCheckpoint(checkpoint);

        // Return the ID of the newly created checkpoint
        return checkpoint.getCheckpointId();
    }

    /**(Taha)
     * Adds an intermediate sprint to a stage.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param stageId  The ID of the stage to which the intermediate sprint checkpoint
     *                 is being added.
     * @param location The kilometre location where the intermediate sprint finishes
     *                 within the stage.
     * @return The ID of the checkpoint created.
     * @throws IDNotRecognisedException   If the ID does not match to any stage in
     *                                    the system.
     * @throws InvalidLocationException   If the location is out of bounds of the
     *                                    stage length.
     * @throws InvalidStageStateException If the stage is "waiting for results".
     * @throws InvalidStageTypeException  Time-trial stages cannot contain any
     *                                    checkpoint.
     */
    @Override
    public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
        
        //init variables
        int i=0;
        int j=0;
        UUID id;
        int checkpointId;
        boolean found = false;
        Stage stage;

        //checking if location is valid
        if(location < 0 || location > getStageLength(stageId)) {
            throw new InvalidLocationException("Invalid location");
        }
        //finding stageID in stages
        while (!found && i < races.size()) {
            Race race = races.get(i);

            while(!found && j< race.getStages().size()) {
                stage = race.getStages().get(j);
                
                //checking stage state
                if(stage.getStageState() == StageState.WAITING_FOR_RESULTS) {
                    throw new InvalidStageStateException("Stage is waiting for results");
                } else if (stage.getStageId() == stageId) {
                    //checking stage type
                    if (stage.getStageType() == StageType.TIME_TRIAL) {
                        throw new InvalidStageTypeException("Time-trial stages cannot contain any checkpoint");
                    } else {
                        //generating checkpointID
                        found = true;
                        id = UUID.randomUUID();
                        checkpointId = (int) id.getMostSignificantBits();
                        stage.addCheckpoint(checkpointId, location, CheckpointType.SPRINT);
                        return checkpointId;
                    }
                }
                j++;
            }
            i++;
        }

        if (!found) {
            throw new IDNotRecognisedException("ID not recognised");
        }
    }

    /**(Suraj)
     * Removes a checkpoint from a stage.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown. (MEANINNG)
     *
     * @param checkpointId The ID of the checkpoint to be removed.
     * @throws IDNotRecognisedException   If the ID does not match to any checkpoint in
     *                                    the system.
     * @throws InvalidStageStateException If the stage is "waiting for results".
     */
    @Override
    public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
        // Iterate over all races
        for (Race race : races) {
            // Iterate over all stages in the race
            for (Stage stage : race.getStages()) {
                // Iterate over all checkpoints in the stage
                for (Checkpoint checkpoint : stage.getCheckpoints()) {
                    // If the ID of the current checkpoint matches the provided checkpointId
                    if (checkpoint.getCheckpointId() == checkpointId) {
                        // If the current stage is in the "waiting for results" state
                        if (stage.getStageState().equals("waiting for results")) {
                            // Throw an exception because checkpoints cannot be removed in this state
                            throw new InvalidStageStateException("Invalid stage state");
                        }
                        // If the stage is not in the "waiting for results" state, remove the checkpoint
                        stage.removeCheckpoint(checkpoint);
                        return;
                    }
                }
            }
        }
        // If the method has not returned after checking all checkpoints, throw an exception because the checkpointId was not found
        throw new IDNotRecognisedException("Checkpoint ID not recognised");
    }

    /**(Taha)
     * Concludes the preparation of a stage. After conclusion, the stage's state
     * should be "waiting for results".
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param stageId The ID of the stage to be concluded.
     * @throws IDNotRecognisedException   If the ID does not match to any stage in
     *                                    the system.
     * @throws InvalidStageStateException If the stage is "waiting for results".
     */
    @Override
    public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
        
        //init variable
        boolean found = false;
        int i=0;
        int j=0;

        //finding stageID in stages
        while(!found && i<races.size()) {
            Race race = races.get(i);
            while(!found && j<race.getStages().size()) {
                Stage stage = race.getStages().get(j);
                if(stage.getStageId() == stageId) {
                    //checking stage state
                    if(stage.getStageState() == StageState.WAITING_FOR_RESULTS) {
                        throw new InvalidStageStateException("Stage is waiting for results");
                    } else {
                        //setting stage state 
                        stage.setStageState(StageState.WAITING_FOR_RESULTS);
                        found = true;
                    }
                }
                j++;
            }
            i++;
        }

        if (!found) {
            //throws IDNotRecognisedException if stageID not found
            throw new IDNotRecognisedException("ID not recognised");
        }
    }

    /**(Suraj)
     * Retrieves the list of checkpoint (mountains and sprints) IDs of a stage.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param stageId The ID of the stage being queried.
     * @return The list of checkpoint IDs ordered (from first to last) by their location in the
     * stage.
     * @throws IDNotRecognisedException If the ID does not match to any stage in the
     *                                  system.
     */
    @Override
    public List<Integer> getStageCheckpoints(int stageId) throws IDNotRecognisedException {
        // Iterate over all races
        for (Race race : races) {
            // Iterate over all stages in the current race
            for (Stage stage : race.getStages()) {
                if (stage.getStageId() == stageId) {
                    List<Checkpoint> checkpoints = stage.getCheckpoints();
                    // Sort the checkpoints based on their location
                    Collections.sort(checkpoints, new Comparator<Checkpoint>() {
                        @Override
                        public int compare(Checkpoint c1, Checkpoint c2) {
                            return Double.compare(c1.getLocation(), c2.getLocation());
                        }
                    });
                    // Create a list to store the IDs of the checkpoints
                    List<Integer> checkpointIds = new ArrayList<>();
                    // Add the ID of each checkpoint to the list
                    for (Checkpoint checkpoint : checkpoints) {
                        checkpointIds.add(checkpoint.getCheckpointId());
                    }

                    // Return the list of checkpoint IDs
                    return checkpointIds;
                }
            }
        }
        // If no stage with the provided stageId is found, throw an exception
        throw new IDNotRecognisedException("ID does not match to any stage in the system");
    }

    /**(Taha)
     * Creates a team with name and description.(INCOMPLETE)
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param name        The identifier name of the team.
     * @param description A description of the team.
     * @return The ID of the created team.
     * @throws IllegalNameException If the name already exists in the platform.
     * @throws InvalidNameException If the name is null, empty, has more than 30
     *                              characters, or has white spaces.
     */
    @Override
    public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
        //checking if duplicate names exist
        for(int i = 0; i<teams.size(); i++) {
            if (name.equals(teams.get(i).getName())) {
                throw new IllegalNameException("Name already exists");
            //checking if name is valid
            } else if (name == null || name.isEmpty() || name.length()>30 || name.contains(" ")) {
                throw new InvalidNameException("Invalid name");
            } else {
                //creating a unique ID for the teams
                UUID id = UUID.randomUUID();
                int teamId = (int) id.getMostSignificantBits();
                ArrayList<Rider> riders = new ArrayList<>();
                Team team = new Team(name, description, riders, teamId);
                teams.add(team);
                return teamId;
            }
        }      
    }

    /**(Suraj)
     * Removes a team from the system.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param teamId The ID of the team to be removed.
     * @throws IDNotRecognisedException If the ID does not match to any team in the
     *                                  system.
     */
    @Override
    public void removeTeam(int teamId) throws IDNotRecognisedException {
        // Initialize a variable to hold the team to be removed
        Team teamToDiscard = null;
        // Iterate over all teams
        for (Team team : teams) {
            // If the ID of the current team matches the provided teamId
            if (team.getTeamId() == teamId) {
                // Set teamToDiscard to the current team and break the loop
                teamToDiscard = team;
                break;
            }
        }
        // If no team with the provided teamId is found
        if (teamToDiscard == null) {
            // Throw an exception
            throw new IDNotRecognisedException("Team ID not recognised");
        }
        // Remove the team from the list of teams
        teams.remove(teamToDiscard);
    }

    /**(Taha)
     * Get the list of teams' IDs in the system.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @return The list of IDs from the teams in the system. An empty list if there
     * are no teams in the system.
     */
    @Override
    public int[] getTeams() {
        //creating an array of teamIDs
        int[] teamIds = new int[teams.size()];
        //adding teamIDs to the array
        for (int i=0; i<teams.size(); i++) {
            teamIds[i] = teams.get(i).getTeamId();
        }
        return teamIds;
    }

    /**(Suraj)==
     * Get the riders of a team.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param teamId The ID of the team being queried.
     * @return A list with riders' ID.
     * @throws IDNotRecognisedException If the ID does not match to any team in the
     *                                  system.
     */
    @Override
    public List<Integer> getTeamRiders(int teamId) throws IDNotRecognisedException {
        // Iterate over all teams
        for (Team team : teams) {
            // If the ID of the current team matches the provided teamId
            if (team.getTeamId() == teamId) {
                // Get the list of riders in the current team
                List<Rider> riders = team.getRiders();
                // Create a list to store the IDs of the riders
                List<Integer> riderIds = new ArrayList<>();
                // Add the ID of each rider to the list
                for (Rider rider : riders) {
                    riderIds.add(rider.getRiderId());
                }
                // Return the list of rider IDs
                return riderIds;
            }
        }
        // If no team with the provided teamId is found, throw an exception
        throw new IDNotRecognisedException("Team ID not recognised");
    }

    /**(Taha)
     * Creates a rider.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param teamID      The ID rider's team.
     * @param name        The name of the rider.
     * @param yearOfBirth The year of birth of the rider.
     * @return The ID of the rider in the system.
     * @throws IDNotRecognisedException If the ID does not match to any team in the
     *                                  system.
     * @throws IllegalArgumentException If the name of the rider is null or empty,
     *                                  or the year of birth is less than 1900.
     */
    @Override
    public int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException, IllegalArgumentException {
        //init variables
        boolean found = false;
        int i=0;
        int riderId;
        Team team;
        //finding teamID in teams
        while (!found && i<teams.size()) {
            if (teams.get(i).getTeamid() == teamID) {
                team = teams.get(i);
                found = true;
                //checking if name and year of birth are valid
                if (name == null || name.isEmpty() || yearOfBirth < 1900) {
                    throw new IllegalArgumentException("Invalid name or year of birth");
                } else {
                    //creating a unique ID for the riders
                    UUID id = UUID.randomUUID();
                    riderId = (int) id.getMostSignificantBits();
                    Rider rider = new Rider(name, yearOfBirth, riderId);
                    team.addRider(rider);
                }
            }i++;
        }
        //throws IDNotRecognisedException if teamID not found
        if (!found) {
            throw new IDNotRecognisedException("ID not recognised");
        }
    return riderId;
    }

    /**(Suraj) #RESULTS IN STAGE
     *  * Removes a rider from the system. When a rider is removed from the platform,
    * all of its results should be also removed. Race results must be updated.
    * <p>
    * The state of this MiniCyclingPortal must be unchanged if any
    * exceptions are thrown.
    *
    * @param riderId The ID of the rider to be removed.
    * @throws IDNotRecognisedException If the ID does not match to any rider in the
    *                                  system.
    */
     * 
    @Override
    public void removeRider(int riderId) throws IDNotRecognisedException {
        // Initialize a variable to hold the rider to be removed
        Rider riderToRemove = null;
        // Iterate over all teams
        for (Team team : teams) {
            for (Rider rider : team.getRiders()) {
                // If the ID of the current rider matches the provided riderId
                if (rider.getRiderId() == riderId) {
                    // remove the rider
                    riderToRemove = rider;
                    team.getRiders().remove(riderToRemove);
                    break;
                }
            }
        }
        // If no rider with the provided riderId is found
        if (riderToRemove == null) {
            // Throw an exception
            throw new IDNotRecognisedException("No rider found with the given ID");
        }

        // Iterate over all races
        for (Race race : races) {
            // Iterate over all stages in the current race
            for (Stage stage : race.getStages()) {
                // Create a list to store the results to be removed
                List<Result> resultsToRemove = new ArrayList<>();
                // Iterate over all results in the current stage
                for (Result result : stage.getResults()) {
                    // If the ID of the rider in the current result matches the provided riderId
                    if (result.getRiderId() == riderId) {
                        // Add the result to the list of results to be removed
                        resultsToRemove.add(result);
                    }
                }
                // Remove all results in the list of results to be removed from the current stage's results
                stage.getResults().removeAll(resultsToRemove);
            }
        }
    }

    /**(Taha)
     * Record the times of a rider in a stage.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param stageId         The ID of the stage the result refers to.
     * @param riderId         The ID of the rider.
     * @param checkpointTimes An array of times at which the rider reached each of the
     *                        checkpoints of the stage, including the start time and the
     *                        finish line.
     * @throws IDNotRecognisedException        If the ID does not match to any rider or
     *                                         stage in the system.
     * @throws DuplicatedResultException       Thrown if the rider has already a result
     *                                         for the stage. Each rider can have only
     *                                         one result per stage.
     * @throws InvalidCheckpointTimesException Thrown if the length of checkpointTimes is
     *                                         not equal to n+2, where n is the number
     *                                         of checkpoints in the stage; +2 represents
     *                                         the start time and the finish time of the
     *                                         stage.
     * @throws InvalidStageStateException      Thrown if the stage is not "waiting for
     *                                         results". Results can only be added to a
     *                                         stage while it is "waiting for results".
     */
    @Override
    public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpointTimes) throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException, InvalidStageStateException {
        // Initialize variables
        boolean found = false;
        Stage stage = null;
        Race race;
        Team team;
        Rider rider;
        //look for stageID in stages
        for (Race r : races) {
            race = r;
            for (Stage s : race.getStages()) {
                if (s.getStageId() == stageId) {
                    stage = s;
                    break;
                }
            }
        }
        //throw IDNotRecognisedException if stageID not found
        if (stage == null) {
            throw new IDNotRecognisedException("Stage ID not recognised");
        }
        //look for riderID in riders
        for (Team t : teams) {
            team = t;
            for (Rider r : team.getRiders()) {
                if (r.getRiderId() == riderId) {
                    rider = r;
                    found = true;
                    break;
                }
            }
            if(found) {
                break;
            }
        }
        //throw IDNotRecognisedException if riderID not found
        if (!found) {
            throw new IDNotRecognisedException("Rider ID not recognised");
        }
        //check if stage is waiting for results
        if (stage.getStageState() != StageState.WAITING_FOR_RESULTS) {
            throw new InvalidStageStateException("Stage is not waiting for results");
        }
        //check if rider already has a result for the stage
        for (Result result : stage.getResults()) {
            if (result.getRiderId() == riderId) {
                throw new DuplicatedResultException("Rider already has a result for the stage");
            }
        }
        //check if checkpointTimes is valid
        if (checkpointTimes.length != stage.getCheckpoints().size() + 2) {
            throw new InvalidCheckpointTimesException("Invalid checkpoint times");
        }
        //create a new result and add it to the stage
        Result result = new Result(stageId, riderId, checkpointTimes);
        stage.addResult(result);
    }

    /**(Suraj) Majorrp
     * Get the times of a rider in a stage.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any exceptions are
     * thrown.
     * @param stageId The ID of the stage the result refers to.
     * @param riderId The ID of the rider.
     * @return The array of times at which the rider reached each of the checkpoints
     * of the stage and the total elapsed time. The elapsed time is the
     * difference between the finish time and the start time. Return an
     * empty array if there is no result registered for the rider in the
     * stage. Assume the total elapsed time of a stage never exceeds 24h
     * and, therefore, can be represented by a LocalTime variable. There is
     * no need to check for this condition or raise any exception.
     * @throws IDNotRecognisedException If the ID does not match to any rider or
     *                                  stage in the system.
     */
    @Override
    public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
        // Initialize an array to store the checkpoint times
        LocalTime[] checkpointTimes = null;

        // Loop through all races
        for (Race race : races) {
            // Loop through all stages in the current race
            for (Stage stage : race.getStages()) {
                // If the ID of the current stage matches the provided stageId
                if (stage.getStageId() == stageId) {
                    // Loop through all results in the current stage
                    for (Result result : stage.getResults()) {
                        // If the ID of the rider in the current result matches the provided riderId
                        if (result.getRiderId() == riderId) {
                            // Get the checkpoint times from the current result
                            checkpointTimes = result.getCheckpointTimes();
                            // Break the loop as we've found the result we're looking for
                            break;
                        }
                    }
                }
            }
        }

        // If no result was found for the provided stageId and riderId
        if (checkpointTimes == null) {
            // Throw an exception
            throw new IDNotRecognisedException("No result found for the given stage ID and rider ID");
        }

        // Calculate the elapsed time as the difference between the finish time and the start time
        LocalTime elapsedTime = checkpointTimes[checkpointTimes.length-1].minusHours(checkpointTimes[0].getHour())
            .minusMinutes(checkpointTimes[0].getMinute()).minusSeconds(checkpointTimes[0].getSecond());

        // Create a new array to store the checkpoint times and the elapsed time
        LocalTime[] times = Arrays.copyOf(checkpointTimes, checkpointTimes.length + 1);

        // Add the elapsed time to the last position of the array
        times[times.length - 1] = elapsedTime;

        // Return the array of times
        return times;
    }

    /**(Taha)
     * For the general classification, the aggregated time is based on the adjusted
     * elapsed time, not the real elapsed time. Adjustments are made to take into
     * account groups of riders finishing very close together, e.p., the peloton. If
     * a rider has a finishing time less than one second slower than the
     * previous rider, then their adjusted elapsed time is the smallest of both. For
     * instance, a stage with 200 riders finishing "together" (i.e., less than 1
     * second between consecutive riders), the adjusted elapsed time of all riders
     * should be the same as the first of all these riders, even if the real gap
     * between the 200th and the 1st rider is much bigger than 1 second. There is no
     * adjustments on elapsed time on time-trials.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param stageId The ID of the stage the result refers to.
     * @param riderId The ID of the rider.
     * @return The adjusted elapsed time for the rider in the stage. Return null if
     * there is no result registered for the rider in the stage.
     * @throws IDNotRecognisedException If the ID does not match to any rider or
     *                                  stage in the system.
     */
    @Override
    public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
        // Initialize variables
        LocalTime adjustedElapsedTime = null;
        Duration onesec = Duration.ofSeconds(1);
        boolean found = false;

        for(Race race : races)
            for (Stage stage : stages) {
                if (stage.getStageId() == stageId) {
                    // Stage found
                    found = true;
                    // Check if the rider participated in this stage
                    boolean riderFoundInStage = false;
                    for (Result result : stage.getResults()) {
                        if (result.getRiderId() == riderId) {
                            riderFoundInStage = true;
                            LocalTime[] checkpointTimes = result.getCheckpointTimes();
                            LocalTime startTime = checkpointTimes[0];
                            LocalTime finishTime = checkpointTimes[checkpointTimes.length - 1];
                            Duration elapsedTime = Duration.between(startTime, finishTime);

                            // Check if stage is Time Trial
                            if (stage.getStageType() == StageType.TT) {
                                return elapsedTime;
                            } else {
                                // Find adjusted elapsed time
                                Duration minElapsedTime = elapsedTime;
                                for (Result otherResult : stage.getResults()) {
                                    if (otherResult.getRiderId() != riderId) {
                                        LocalTime[] otherCheckpointTimes = otherResult.getCheckpointTimes();
                                        LocalTime otherStartTime = otherCheckpointTimes[0];
                                        LocalTime otherFinishTime = otherCheckpointTimes[otherCheckpointTimes.length - 1];
                                        Duration otherElapsedTime = Duration.between(otherStartTime, otherFinishTime);

                                        // Check if elapsed time needs adjustment
                                        if (otherElapsedTime.minus(elapsedTime).compareTo(onesec) <= 0) {
                                            minElapsedTime = elapsedTime.minus(Duration.ofSeconds(1));
                                            break;
                                        }
                                    }
                                }
                                adjustedElapsedTime = minElapsedTime;
                            }
                        }
                    }
                    if (!riderFoundInStage) {
                        // Rider did not participate in this stage
                        return null;
                }
            }
        }
        // Check if stage or rider not found
        if (!found) {
            throw new IDNotRecognisedException("ID not recognised");
        }
        return adjustedElapsedTime;
}
        

    /**(Suraj)
     * Removes the stage results from the rider.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param stageId The ID of the stage the result refers to.
     * @param riderId The ID of the rider.
     * @throws IDNotRecognisedException If the ID does not match to any rider or
     *                                  stage in the system.
     */
    @Override
    public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
        // Initialize a flag to track if a result was found
        boolean resultFound = false;

        // Loop through all races
        for (Race race : races) {
            // Loop through all stages in the current race
            for (Stage stage : race.getStages()) {
                // If the ID of the current stage matches the provided stageId
                if (stage.getStageId() == stageId) {
                    // Loop through all results in the current stage
                    for (Result result : stage.getResults()) {
                        // If the ID of the rider in the current result matches the provided riderId
                        if (result.getRiderId() == riderId) {
                            // Remove the current result from the stage's results
                            stage.getResults().remove(result);
                            // Set the flag to true as we've found and removed the result
                            resultFound = true;
                            // Break the loop as we've found the result we're looking for
                            break;
                        }
                    }
                }
            }
        }

        // If no result was found for the provided stageId and riderId
        if (!resultFound) {
            // Throw an exception
            throw new IDNotRecognisedException("No results found for the given stage and rider IDs");
        }
    }



    /**(Taha)
     * Get the riders finished position in a a stage.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param stageId The ID of the stage being queried.
     * @return A list of riders ID sorted by their elapsed time. An empty list if
     * there is no result for the stage.
     * @throws IDNotRecognisedException If the ID does not match any stage in the
     *                                  system.
     */
    @Override
    public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
        //init variables
        Race race;
        Stage stage;
        boolean found = false;
        //finding stageID in stages
        for (int i = 0; i < races.size(); i++) {
            race = races.get(i);
            for (int j = 0; j < race.getStages().size(); j++) {
                if (race.getStages().get(j).getStageId() == stageId) {
                    stage = race.getStages().get(j);
                    found = true;
                    //check if stage has results
                    if (stage.getResults().isEmpty()) {
                        return new int[0];
                    } else {
                        //create a list of riders rank
                        ArrayList<Integer> ridersRank = new ArrayList<>();
                        for (Result result : stage.getResults()) {
                            ridersRank.add(result.getRiderId());
                        }

                    // compare the elapsed time of each rider
                        for (int m = 0; m < ridersRank.size() - 1; m++) {
                            for (int n = 0; n < ridersRank.size() - m - 1; n++) {
                                LocalTime[] checkpointTimes1 = stage.getResults().get(n).getCheckpointTimes();
                                LocalTime[] checkpointTimes2 = stage.getResults().get(n + 1).getCheckpointTimes();
                                Duration elapsedTime1 = Duration.between(checkpointTimes1[0], checkpointTimes1[checkpointTimes1.length - 1]);
                                Duration elapsedTime2 = Duration.between(checkpointTimes2[0], checkpointTimes2[checkpointTimes2.length - 1]);

                                if (elapsedTime1.compareTo(elapsedTime2) > 0) {
                                    // Swap riders
                                    int temp = ridersRank.get(n);
                                    ridersRank.set(n, ridersRank.get(n + 1));
                                    ridersRank.set(n + 1, temp);
                                }
                            }
                        }
                        //convert ridersRank to an array
                        int[] ridersRankArray = new int[ridersRank.size()];
                        for (int k = 0; k < ridersRank.size(); k++) {
                            ridersRankArray[k] = ridersRank.get(k);
                        }
                        return ridersRankArray;
                }
            }
        }
    }
    //throws IDNotRecognisedException if stageID not found
    if (!found) {
    throw new IDNotRecognisedException("ID does not match any stage in the system");
    }
}

    /**(Suraj)
     * Get the adjusted elapsed times of riders in a stage.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any exceptions are
     * thrown.
     * @param stageId The ID of the stage being queried.
     * @return The ranked list of adjusted elapsed times sorted by their finish
     * time. An empty list if there is no result for the stage. These times
     * should match the riders returned by
     * {@link #getRidersRankInStage(int)}. Assume the total elapsed time of
     * in a stage never exceeds 24h and, therefore, can be represented by a
     * LocalTime variable. There is no need to check for this condition or
     * raise any exception.
     * @throws IDNotRecognisedException If the ID does not match any stage in the
     *                                  system.
     */
    // Declare a Stage object and a boolean flag to check if the stage is found
   
    @Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
        // Declare a Stage object and a boolean flag to check if the stage is found
        Stage stage = null; 
        boolean found = false;

    // Loop through all races and their stages to find the stage with the given ID
    for (Race race : races) {
        for (stage : race.getStages()) {
            if (stage.getStageId() == stageId) {
                found = true;
                break;
            }
        }
        if (found) break;
    }

    // If the stage is not found, throw an exception
    if (!found) {
        throw new IDNotRecognisedException("The provided stage ID does not match any stage in the system.");
    }

    // If the stage has no results, return an empty array
    if (stage.getResults().isEmpty()) {
        return new LocalTime[0];
    } else {
        // Create a map of rider finish times
        Map<Result, LocalTime> finishTimes = new HashMap<>();
        for (Result result : stage.getResults()) {
            LocalTime[] checkpointTimes = result.getCheckpointTimes();
            LocalTime finishTime = checkpointTimes[checkpointTimes.length - 1];
            finishTimes.put(result, finishTime);
        }

        // Sort the results by finish time
        List<Result> sortedResults = stage.getResults().stream()
            .sorted(Comparator.comparing(finishTimes::get))
            .collect(Collectors.toList());

        // Map the sorted results to their adjusted elapsed times
        List<LocalTime> adjustedTimes = sortedResults.stream()
            .map(result -> getRiderAdjustedElapsedTimeInStage(stageId, result.getRiderId()))
            .collect(Collectors.toList());

        // Convert the list to an array and return
        return adjustedTimes.toArray(new LocalTime[0]);
        }
    }
    }
                   


    /**(Taha)
     * Get the number of points obtained by each rider in a stage.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param stageId The ID of the stage being queried.
     * @return The ranked list of points each riders received in the stage, sorted
     * by their elapsed time. An empty list if there is no result for the
     * stage. These points should match the riders returned by
     * {@link #getRidersRankInStage(int)}.
     * @throws IDNotRecognisedException If the ID does not match any stage in the
     *                                  system.
     */
   
   @Override
    public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
    
    //init variables
    Race race;
    Stage stage = null;
    boolean found = false;
    Integer points;
    List<Map.Entry<LocalTime, Integer>> ridersPoints = new ArrayList<>();

    //finding stageID in stages
    for (int i = 0; i < races.size(); i++) {
        race = races.get(i);
        for (int j = 0; j < race.getStages().size(); j++) {
            if (race.getStages().get(j).getStageId() == stageId) {
                stage = race.getStages().get(j);
                found = true;
                break; 
            }
        }
        if (found) {
            break;
        }
    }
    //throws IDNotRecognisedException if stageID not found
    if (!found || stage == null) {
        throw new IDNotRecognisedException("StageID not found");
    } else if (stage.getResults().isEmpty()) {
        return new int[0];
    }
    //get the points of each rider in the stage
    for (Result result : stage.getResults()) {
        LocalTime[] checkpointTimes = result.getCheckpointTimes();
        LocalTime elapsedTime = checkpointTimes[checkpointTimes.length - 1].minusSeconds(checkpointTimes[0].toSecondOfDay());
        points = result.getPoints();
        ridersPoints.add(Map.entry(elapsedTime, points));
    }
    //sort the ridersPoints by elapsed time
    Collections.sort(ridersPoints, Map.Entry.comparingByKey());
    //create an array of sorted points
    int[] sortedPoints = ridersPoints.stream().mapToInt(Map.Entry::getValue).toArray();

    return sortedPoints;
    }

    


    /**(Suraj)
     * Get the number of mountain points obtained by each rider in a stage.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param stageId The ID of the stage being queried.
     * @return The ranked list of mountain points each riders received in the stage,
     * sorted by their finish time. An empty list if there is no result for
     * the stage. These points should match the riders returned by
     * {@link #getRidersRankInStage(int)}.
     * @throws IDNotRecognisedException If the ID does not match any stage in the
     *                                  system.
     */
    @Override
    public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
        // Create a list to store pairs of mountain points and finish times
        List<Pair<Integer, LocalTime>> mountainPointsAndTimes = new ArrayList<>();

        // Flag to check if the stage is found
        boolean found = false;

        // Loop through all races and their stages to find the stage with the given ID
        for (Race race : races) {
            for (Stage stage : race.getStages()) {
                if (stage.getStageId() == stageId) {
                    found = true;
                    // If the stage is not a mountain stage, throw an exception
                    if (stage.getStageType() != StageType.HIGH_MOUNTAIN || stage.getStageType() != StageType.MEDIUM_MOUNTAIN) 
                        throw new InvalidStageTypeException("Stage provided is not a mountain stage");
                    else {
                        // For each result in the stage, get the rider's mountain points and finish time
                        for (Result result : stage.getResults()) {
                            int points = result.getPoints();
                            LocalTime finishTime = result.getCheckpointTimes().get(result.getCheckpointTimes().size() - 1);
                            // Add the points and finish time to the list
                            mountainPointsAndTimes.add(new Pair<>(points, finishTime));
                        }
                    }
                    break;
                }
            }
            if (found) break;
        }

        // If the stage is not found, throw an exception
        if (!found) {
            throw new IDNotRecognisedException("The provided stage ID does not match any stage in the system.");
        }

        // Sort the list by finish time
        mountainPointsAndTimes.sort(Comparator.comparing(Pair::getSecond));

        // Map the sorted list to an array of mountain points and return
        return mountainPointsAndTimes.stream().mapToInt(Pair::getFirst).toArray();
    }

    /** (Taha)
     * Method empties this MiniCyclingPortal of its contents and resets all
     * internal counters.
     */
    @Override
    public void eraseCyclingPortal() {
        // Clear the lists of teams and races
        teams.clear();
        races.clear();
        
        // Reset the counters
        teamCounter = 0;
        raceCounter = 0;  
    }
        
    /**(Suraj)
     * Method saves this MiniCyclingPortal contents into a serialised file,
     * with the filename given in the argument.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param filename Location of the file to be saved.
     * @throws IOException If there is a problem experienced when trying to save the
     *                     store contents to the file.
     */
    @Override
    public void saveCyclingPortal(String filename) throws IOException {
        // Create a backup of the current state of the portal
        MiniCyclingPortal backupState = this.clone();

        try (
            // Create a FileOutputStream to write to the specified file
            FileOutputStream fileOut = new FileOutputStream(filename);
            // Create an ObjectOutputStream to write objects to the FileOutputStream
            ObjectOutputStream out = new ObjectOutputStream(fileOut)
        ) {
            // Write the current state of the portal to the ObjectOutputStream
            out.writeObject(this);
        } catch (IOException i) {
            // If an IOException occurs, restore the state of the portal from the backup
            this.restoreFromBackup(backupState);
            // Rethrow the IOException with a custom message
            throw new IOException("Error occurred while saving the portal", i);
        }
    }

    /**(Taha)
     * Method should load and replace this MiniCyclingPortal contents with the
     * serialised contents stored in the file given in the argument.
     * <p>
     * The state of this MiniCyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param filename Location of the file to be loaded.
     * @throws IOException            If there is a problem experienced when trying
     *                                to load the store contents from the file.
     * @throws ClassNotFoundException If required class files cannot be found when
     *                                loading.
     */
   @Override
    public void loadCyclingPortal(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            
            // Read the objects from the file
            ArrayList<Team> loadedTeams = (ArrayList<Team>) ois.readObject();
            ArrayList<Race> loadedRaces = (ArrayList<Race>) ois.readObject();

            // Replace the current state of the portal with the loaded state
            this.teams = loadedTeams;
            this.races = loadedRaces;
            // Update the counter
            this.teamCounter = this.teams.size();
            this.raceCounter = this.races.size();
        }
        // Catch error if file not found
         catch (IOException e) {
            System.err.println("An error occurred while reading from the file: " + e.getMessage());
        } 
        // catch error if class not found
        catch (ClassNotFoundException e) {
            System.err.println("Class not found while deserializing objects: " + e.getMessage());
        }
    }

    /**(Suraj)
     * The method removes the race and all its related information, i.e., stages,
     * checkpoints, and results.
     * <p>
     * The state of this CyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param name The name of the race to be removed.
     * @throws NameNotRecognisedException If the name does not match to any race in
     *                                    the system.
     */
    @Override
    public void removeRaceByName(String name) throws NameNotRecognisedException {
        // Initialize a variable to hold the race to be removed
        Race raceToRemove = null;

        // Iterate over all races
        for (Race race : races) {
            // If a race with the given name is found, set it to be removed and break the loop
            if (race.getRaceName().equals(name)) {
                raceToRemove = race;
                break;
            }
        }

        // If no race with the given name is found, throw an exception
        if (raceToRemove == null) {
            throw new NameNotRecognisedException("No race found with name: " + name);
        }

        // For the race to be removed, iterate over all its stages
        for (Stage stage : raceToRemove.getStages()) {
            // Clear the results and checkpoints of each stage
            stage.getResults().clear();
            stage.getCheckpoints().clear();
        }

        // Clear the stages of the race
        raceToRemove.getStages().clear();

        // Finally, remove the race from the races collection
        races.remove(raceToRemove);
    }

    /**(Taha)
     * Get the general classification rank of riders in a race.
     * <p>
     * The state of this CyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param raceId The ID of the race being queried.
     * @return A ranked list of riders' IDs sorted ascending by the sum of their
     * adjusted elapsed times in all stages of the race. That is, the first
     * in this list is the winner (least time). An empty list if there is no
     * result for any stage in the race.
     * @throws IDNotRecognisedException If the ID does not match any race in the
     *                                  system.
     */
    @Override
    public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
        
        // Initialize variables
        Race race = null;
        boolean empty = false;
        boolean found = false;
        Stage stage;
        Result result;
        Map<Integer, LocalTime> riderTimesMap = new HashMap<>();

        // finding raceID
        for (int i=0; i<races.size(); i++) {
            if (races.get(i).getRaceId() == raceId) {
                race = races.get(i);
                found = true;
                break;
            }
        }
        //throw exception if raceID not found
        if (!found || race == null) {
            throw new IDNotRecognisedException("Race ID not recognised");
        }
        // iterate over staegs
        for (int j=0; j<race.getStages().size(); j++) {
            //check if stage has results
            if (race.getStages().get(j).getResults() == null || race.getStages().get(j).getResults().isEmpty()) {
                empty = true;
                continue;
            } else{
                stage = race.getStages().get(j); 
                empty = false;
            }
            for (int k=0; k<stage.getResults().size(); k++) {
                result = stage.getResults().get(k);
                //check if riderID is in the map
                if (!riderTimesMap.containsKey(result.getRiderId())) {
                    //add riderID and adjusted elapsed time to the map
                    riderTimesMap.put(result.getRiderId(), getRiderAdjustedElapsedTimeInStage(stage.getStageId(), result.getRiderId())); // Corrected method call
                } else {
                    //update the adjusted elapsed time of the rider
                    Duration existingTime = Duration.between(LocalTime.MIN, riderTimesMap.get(result.getRiderId()));
                    Duration newTime = Duration.between(LocalTime.MIN, getRiderAdjustedElapsedTimeInStage(stage.getStageId(), result.getRiderId())); // Corrected method call
                    riderTimesMap.put(result.getRiderId(), LocalTime.MIN.plus(existingTime).plus(newTime));
                }
            }
        }

        if (empty) {
            return new int[0];
        }
        //sort the riders by adjusted elapsed time
        List<Map.Entry<Integer, LocalTime>> sortedRidersList = new ArrayList<>(riderTimesMap.entrySet());
        sortedRidersList.sort(Map.Entry.comparingByValue());

        //create an array of sorted riders
        int n = sortedRidersList.size();
        int[] rankedRiders = new int[n];
        for (int l = 0; l < n; l++) {
            rankedRiders[l] = sortedRidersList.get(l).getKey();
        }

        return rankedRiders;
    }

    /**(Suraj)
     * Get the general classification times of riders in a race.
     * <p>
     * The state of this CyclingPortal must be unchanged if any exceptions are
     * thrown.
     *
     * @param raceId The ID of the race being queried.
     * @return A list of riders' times sorted by the sum of their adjusted elapsed
     * times in all stages of the race. An empty list if there is no result
     * for any stage in the race. These times should match the riders
     * returned by {@link #getRidersGeneralClassificationRank(int)}. Assume
     * the total elapsed time of a race (the sum of all of its stages) never
     * exceeds 24h and, therefore, can be represented by a LocalTime
     * variable. There is no need to check for this condition or raise any
     * exception.
     * @throws IDNotRecognisedException If the ID does not match any race in the
     *                                  system.
     */
    @Override
    public LocalTime [] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
        // Initialize a variable to hold the target race
        Race targetRace = null;

        // Iterate over all races
        for (Race race : races) {
            // If a race with the given ID is found, set it as the target race and break the loop
            if (race.getRaceId() == raceId) {
                targetRace = race;
                break;
            }
        }

        // If no race with the given ID is found, throw an exception
        if (targetRace == null) {
            throw new IDNotRecognisedException("No race found with ID: " + raceId);
        }

        // Initialize a map to hold the total elapsed time for each rider
        Map<Integer, LocalTime> riderTimes = new HashMap<>();

        // Iterate over all stages of the target race
        for (Stage stage : targetRace.getStages()) {
            // Iterate over all results of each stage
            for (Result result : stage.getResults()) {
                // Get the rider ID and checkpoint times from the result
                int riderId = result.getRiderId();
                LocalTime[] checkpointTimes = result.getCheckpointTimes();

                // Calculate the elapsed time for the stage
                LocalTime elapsedTime = checkpointTimes[checkpointTimes.length - 1].minus(checkpointTimes[0]);

                // Get the previous total elapsed time for the rider, or 0 if none exists
                LocalTime previousTime = riderTimes.getOrDefault(riderId, LocalTime.of(0, 0));

                // Add the elapsed time for the stage to the total elapsed time for the rider
                riderTimes.put(riderId, previousTime.plusHours(elapsedTime.getHour())
                                                     .plusMinutes(elapsedTime.getMinute())
                                                     .plusSeconds(elapsedTime.getSecond()));
            }
        }

        // Create a list of the total elapsed times for all riders
        List<LocalTime> sortedTimes = new ArrayList<>(riderTimes.values());

        // Sort the list in ascending order
        Collections.sort(sortedTimes);

        // Convert the list to an array of LocalTime
        LocalTime[] sortedTimesArray = sortedTimes.toArray(new LocalTime[0]);

        // Return the sorted array of total elapsed times
        return sortedTimesArray;
    }

    /**(Taha)
     * Get the overall points of riders in a race.
     * <p>
     * The state of this CyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param raceId The ID of the race being queried.
     * @return An array of riders' points (i.e., the sum of their points in all stages
     * of the race), sorted by the total adjusted elapsed time. An empty array if
     * there is no result for any stage in the race. These points should
     * match the riders returned by {@link #getRidersGeneralClassificationRank(int)}.
     * @throws IDNotRecognisedException If the ID does not match any race in the
     *                                  system.
     */
    @Override
    public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
        //init variables
        Race race = null;
        boolean found = false;
        boolean empty = true;
        Map<Integer, Integer> ridersPoints = new HashMap<>(); 
        Map<Integer, LocalTime> riderElapsedTimes = new HashMap<>(); 

        //finding raceID
        for (Race r : races) {
            if (r.getRaceId() == raceId) {
                race = r;
                found = true;
                break;
            }
        }
        //throw exception if raceID not found
        if (!found || race == null) {
            throw new IDNotRecognisedException("Race ID not recognised");
        }
        //iterate over stages
        for (Stage stage : race.getStages()) {
            //check if stage has results
            if (!stage.getResults().isEmpty()) {
                empty = false;
                for (Result result : stage.getResults()) {
                    int riderId = result.getRiderId();
                    int points = result.getPoints();
                    LocalTime elapsedTime = getRiderAdjustedElapsedTimeInStage(stage.getStageId(), riderId);
                    
                    //add points to the rider
                    ridersPoints.put(riderId, ridersPoints.getOrDefault(riderId, 0) + points);
                    
                    //add adjusted elapsed time to the rider
                    riderElapsedTimes.put(riderId, riderElapsedTimes.getOrDefault(riderId, LocalTime.MIN).plusSeconds(elapsedTime.toSecondOfDay()));
                }
            }
        }

        if (empty) {
            return new int[0]; // No results available
        }

    
        List<Map.Entry<Integer, Integer>> sortedRiders = new ArrayList<>(ridersPoints.entrySet());
        
        // Sort the list based on total elapsed time
        Collections.sort(sortedRiders, (a, b) -> riderElapsedTimes.get(a.getKey()).compareTo(riderElapsedTimes.get(b.getKey())));

        // Extracting points from sorted list
        int[] totalPoints = sortedRiders.stream().mapToInt(Map.Entry::getValue).toArray();

        return totalPoints;
    }

    /**(Suraj)
     * Get the overall mountain points of riders in a race.
     * <p>
     * The state of this CyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param raceId The ID of the race being queried.
     * @return An array of riders' mountain points (i.e., the sum of their mountain
     * points in all stages of the race), sorted by the total adjusted elapsed time.
     * An empty array if there is no result for any stage in the race. These
     * points should match the riders returned by
     * {@link #getRidersGeneralClassificationRank(int)}.
     * @throws IDNotRecognisedException If the ID does not match any race in the
     *                                  system.
     */
    @Override
    public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
        Race race = null;
        boolean found = false;
        boolean empty = true;
        List<Map.Entry<LocalTime, Integer>> ridersList = new ArrayList<>();

        for (Race r : races) {
            if (r.getRaceId() == raceId) {
                race = r;
                found = true;
                break;
            }
        }

        if (!found || race == null) {
            throw new IDNotRecognisedException("Race ID not recognised");
        }

        for (Stage stage : race.getStages()) {
            if (!stage.getResults().isEmpty()) {
                empty = false;
                for (Result result : stage.getResults()) {
                    LocalTime elapsedTime = getRiderAdjustedElapsedTimeInStage(stage.getStageId(), result.getRiderId());
                    int points = result.getPoints();
                    ridersList.add(Map.entry(elapsedTime, points));
                }
            }
        }

        if (empty) {
            return new int[0]; // No results available
        }

        // Sort the list based on elapsed time
        Collections.sort(ridersList, Map.Entry.comparingByKey());

        // Extracting points from sorted list
        int[] totalPoints = ridersList.stream().mapToInt(Map.Entry::getValue).toArray();

        return totalPoints;
    }


    /**(Taha)
     * Get the ranked list of riders based on the points classification in a race.
     * <p>
     * The state of this CyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param raceId The ID of the race being queried.
     * @return A ranked list of riders' IDs sorted descending by the sum of their
     * points in all stages of the race. That is, the first in this list is
     * the winner (more points). An empty list if there is no result for any
     * stage in the race.
     * @throws IDNotRecognisedException If the ID does not match any race in the
     *                                  system.
     */
    @Override
    public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
    // Initialize variables
    Race race = null;
    boolean empty = true;
    boolean found = false;
    Stage stage;
    Result result;
    Map<Integer, Integer> riderPointsMap = new HashMap<>();

    // Search for the race with the given ID
    for (int i = 0; i < races.size(); i++) {
        if (races.get(i).getRaceId() == raceId) {
            race = races.get(i);
            found = true;
            break;
        }
    }
    //throw exception if raceID not found
    if (!found || race == null) {
        throw new IDNotRecognisedException("Race ID not recognised");
    }

    //iterate over stages
    for (int j = 0; j < race.getStages().size(); j++) {
        stage = race.getStages().get(j);
        //check if stage has results
        if (stage.getResults() == null || stage.getResults().isEmpty()) {
            continue; 
        }
        empty = false; 
        //iterate over results
        for (int k = 0; k < stage.getResults().size(); k++) {
            result = stage.getResults().get(k);
            int riderId = result.getRiderId();
            int points = result.getPoints();
            riderPointsMap.put(riderId, riderPointsMap.getOrDefault(riderId, 0) + points);
        }
    }

    // Check if no stages had results
    if (empty) {
        return new int[0];
    }

    // Sort rider points in descending order
    List<Integer> sortedPoints = new ArrayList<>(riderPointsMap.values());
    Collections.sort(sortedPoints, Collections.reverseOrder());

    // Convert sorted points to array
    int[] ranks = sortedPoints.stream().mapToInt(Integer::intValue).toArray();
    return ranks;
}
    

    /**(Suraj)
     * Get the ranked list of riders based on the mountain classification in a race.
     * <p>
     * The state of this CyclingPortal must be unchanged if any
     * exceptions are thrown.
     *
     * @param raceId The ID of the race being queried.
     * @return A ranked list of riders' IDs sorted descending by the sum of their
     * mountain points in all stages of the race. That is, the first in this
     * list is the winner (more points). An empty list if there is no result
     * for any stage in the race.
     * @throws IDNotRecognisedException If the ID does not match any race in the
     *                                  system.
     */
    @Override
    public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
        // Flag
        Race foundRace = null;

        // Loop through all races
        for (Race race : races) {
            // If the current race's ID matches the given ID, store the race and break the loop
            if (race.getRaceId() == raceId) {
                foundRace = race;
                break;
            }
        }

        // If no race was found, throw an exception
        if (foundRace == null) {
            throw new IDNotRecognisedException("The provided race ID does not match any race in the system.");
        }

        // Initialize a map to store each rider's total mountain points
        Map<Integer, Integer> riderPoints = new HashMap<>();

        // Loop through all stages in the found race
        for (Stage stage : foundRace.getStages()) {
            // Loop through all results in the current stage
            for (Result result : stage.getResults()) {
                // Get the rider's ID and mountain points
                int riderId = result.getRiderId();
                int points = result.getPoints();

                // Add the points to the rider's total in the map
                riderPoints.put(riderId, riderPoints.getOrDefault(riderId, 0) + points);
            }
        }

        // Convert the map to a stream, sort it in descending order by value (mountain points), map it to an array of rider IDs, and return it
        return riderPoints.entrySet().stream()
            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
            .mapToInt(Map.Entry::getKey)
            .toArray();
    }
    }

// It was updated here.