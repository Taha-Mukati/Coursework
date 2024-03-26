package cycling;


import java.util.AbstractMap;
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





public class CyclingPortalImpl implements CyclingPortal {

    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<Race> races = new ArrayList<>();
    
    Integer teamCounter = 0;
    Integer raceCounter = 0;


   
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

    
    @Override
    public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
        //init variables
        int raceId = 0;
        //checking if duplicate names exist
        for(int i = 0; i<races.size(); i++) {
            if (name.equals(races.get(i).getRaceName())) {
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
        ArrayList<Stage> stages = new ArrayList<>();
        double totalLength = 0;
        Race race = new Race(name, raceId, description, totalLength, teams, stages);
        races.add(race);
        raceCounter+=1;
        return raceId;
    }

   
    @Override
    public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
    
    //init variable
    boolean found = false;
    int i=0;
    String details = "";

    //looking for raceIDs in races
    while (!found && i<races.size()) {
        if (raceId == races.get(i).getRaceId()) {
            found = true;
            //converting Race toString
            details = "Race Details = (Race ID  = " + races.get(i).getRaceId() + ", Name = " + races.get(i).getRaceName() + 
            ", Description = " + races.get(i).getRaceDescription() + ", Number of Stages = " 
            + getNumberOfStages(raceId) + ", Total Length = " + races.get(i).getTotalLength() +")"; 
            } i++;
        }
        if (!found) {
            throw new IDNotRecognisedException("Race ID not recognised");
        }
        return details;
    }


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
            throw new IDNotRecognisedException("Race ID not recognised");
        }
    }

   
    @Override
    public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
        //init variables
        boolean found = false;
        int i=0;
        int numberOfStages = 0; // Initialize numberOfStages here
        //finding RaceID in races and getting number of stages
        while (!found && i<races.size()) {
            if (races.get(i).getRaceId() == raceId) {
                found = true;
                numberOfStages = races.get(i).getStages().size();
            }
            i++;
        }
        if (!found) {
            // throws IDnotRecognisedException if ID not found
            throw new IDNotRecognisedException("Race ID not recognised");
        } else {
            return numberOfStages;
        }
    }

    
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
    
    @Override
    public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
        //init variables
        int[] stageIds = new int[0];
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
            }
            i++;      
        }

        if (!found) {
            //throws IDNotRecognisedException if race ID not found
            throw new IDNotRecognisedException("Race ID not recognised");
        } else {
            return stageIds;
        }
    }

    
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

    
    @Override
    public void removeStageById(int stageId) throws IDNotRecognisedException {
        //init variables
        int i=0;
        int j=0;
        boolean found = false;
        ArrayList<Stage> stages;
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
        if (stage.getStageType().equals(StageType.TT)) {
            throw new InvalidStageTypeException("Time-trial stages cannot contain any checkpoint");
        }

        // Generate a unique ID for the checkpoint
        UUID id = UUID.randomUUID();
        int checkpointId = (int) id.getMostSignificantBits();
        // Create a new climb
        // Add the checkpoint to the stage
        stage.addClimbCheckpoint(checkpointId, stageId, location, type, averageGradient, length);

        // Return the ID of the newly created checkpoint
        return checkpointId;
    }

    
    @Override
    public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
        
        //init variables
        int i=0;
        int j=0;
        UUID id;
        int checkpointId=0;
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
                    if (stage.getStageType() == StageType.TT) {
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
        return checkpointId;
    }

   
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

    
    @Override
    public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
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

                    // Convert the list of checkpoint IDs to an array and return it
                    int[] checkpointIdsArray = checkpointIds.stream().mapToInt(i->i).toArray();
                    return checkpointIdsArray;
                }
            }
        }
        // If no stage with the provided stageId is found, throw an exception
        throw new IDNotRecognisedException("ID does not match to any stage in the system");
    }
       
    
    @Override
    public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
        int teamId = 0;
        //checking if duplicate names exist
        for(int i = 0; i<teams.size(); i++) {
            if (name.equals(teams.get(i).getTeamName())) {
                throw new IllegalNameException("Name already exists");
            //checking if name is valid
            } else if (name == null || name.isEmpty() || name.length()>30 || name.contains(" ")) {
                throw new InvalidNameException("Invalid name");
            } else {
                //creating a unique ID for the teams
                UUID id = UUID.randomUUID();
                teamId = (int) id.getMostSignificantBits();
                ArrayList<Rider> riders = new ArrayList<>();
                Team team = new Team(name, teamId, riders, description);
                teams.add(team);
            }
        }
        return teamId;      
    }

    
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

    
    @Override
    public int [] getTeamRiders(int teamId) throws IDNotRecognisedException {
        // Iterate over all teams
        for (Team team : teams) {
            // If the ID of the current team matches the provided teamId
            if (team.getTeamId() == teamId) {
                // Get the list of riders in the current team
                List<Rider> riders = team.getRiders();
                // Create a list to store the IDs of the riders
                int[] riderIds = new int[riders.size()];
                // Add the ID of each rider to the list
                for (int i=0; i<riders.size(); i++) {
                    riderIds[i] = riders.get(i).getRiderId();
                }
                // Return the list of rider IDs
                return riderIds;
            }
        }
        // If no team with the provided teamId is found, throw an exception
        throw new IDNotRecognisedException("Team ID not recognised");
    }

    
    @Override
    public int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException, IllegalArgumentException {
        //init variables
        boolean found = false;
        int i=0;
        Team team;
        int riderId = 0;
        //finding teamID in teams
        while (!found && i<teams.size()) {
            if (teams.get(i).getTeamId() == teamID) {
                team = teams.get(i);
                found = true;
                //checking if name and year of birth are valid
                if (name == null || name.isEmpty() || yearOfBirth < 1900) {
                    throw new IllegalArgumentException("Invalid name or year of birth");
                } else {
                    //creating a unique ID for the riders
                    UUID id = UUID.randomUUID();
                    riderId = (int) id.getMostSignificantBits();
                    Rider rider = new Rider(name, riderId, yearOfBirth);
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

    
    @Override
    public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpointTimes) throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException, InvalidStageStateException {
        // Initialize variables
        boolean found = false;
        Stage stage = null;
        Race race;
        Team team;
        Rider rider;
        int points = 0;
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
        stage.addResult(stageId, riderId, checkpointTimes, points);
    }

    
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

    
    @Override
    public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
        // Initialize variables
        LocalTime adjustedElapsedTime = null;
        Duration onesec = Duration.ofSeconds(1);
        boolean found = false;

        for (Race race : races) {
            for (Stage stage : race.getStages()) {
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
                                return finishTime;
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
                                adjustedElapsedTime = startTime.plus(minElapsedTime);
                            }
                        }
                    }
                    if (!riderFoundInStage) {
                        // Rider did not participate in this stage
                        return null;
                    }
                }
            }
        }
        // Check if stage or rider not found
        if (!found) {
            throw new IDNotRecognisedException("ID not recognised");
        }
        return adjustedElapsedTime;
    }
        

    
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



    
    @Override
    public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
        
        //init variables
        Race race;
        Stage stage=null;
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
                    }
                }
            }
        }
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
                    

    //throws IDNotRecognisedException if stageID not found
    if (!found) {
    throw new IDNotRecognisedException("ID does not match any stage in the system");
    }
        return ridersRankArray;
    }
    
    
   
    @Override
    public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
        // Declare a Stage object and a boolean flag to check if the stage is found
        Stage foundStage = null;
        boolean found = false;

        // Loop through all races and their stages to find the stage with the given ID
        for (Race race : races) {
            for (Stage stage : race.getStages()) {
                if (stage.getStageId() == stageId) {
                    found = true;
                    foundStage = stage;
                    break;
                }
            }
            if (found) break;
        }

        // If the stage is not found, throw an exception
        if (!found) {
            throw new IDNotRecognisedException("The provided stage ID does not match any stage in the system.");
        }

        if (foundStage.getResults().isEmpty()) {
            return new LocalTime[0];
        } else {
            // Create a map of rider finish times
            Map<Result, LocalTime> finishTimes = new HashMap<>();
            for (Result result : foundStage.getResults()) {
                LocalTime[] checkpointTimes = result.getCheckpointTimes();
                LocalTime finishTime = checkpointTimes[checkpointTimes.length - 1];
                finishTimes.put(result, finishTime);
            }

            // Sort the results by finish time
            List<Result> sortedResults = foundStage.getResults().stream()
                .sorted(Comparator.comparing(finishTimes::get))
                .collect(Collectors.toList());

            // Map the sorted results to their adjusted elapsed times
            List<LocalTime> adjustedTimes = sortedResults.stream()
                .map(result -> {
                    try {
                        return getRiderAdjustedElapsedTimeInStage(stageId, result.getRiderId());
                    } catch (IDNotRecognisedException e) {
                        // Handle the exception here
                        // You can log the error or perform any other necessary actions
                        return null; // Return a default value or handle the exception case
                    }
                })
                .collect(Collectors.toList());

            // Convert the list to an array and return
            return adjustedTimes.toArray(new LocalTime[0]);
        }
    }
                   


   
   
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

    


    @Override
    public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
        // Create a list to store pairs of mountain points and finish times
        List<AbstractMap.SimpleEntry<Integer, LocalTime>> mountainPointsAndTimes = new ArrayList<>();

        // Flag to check if the stage is found
        boolean found = false;

        // Loop through all races and their stages to find the stage with the given ID
        for (Race race : races) {
            for (Stage stage : race.getStages()) {
                if (stage.getStageId() == stageId) {
                    found = true;
                    // If the stage is not a mountain stage, throw an exception
                        // For each result in the stage, get the rider's mountain points and finish time
                    for (Result result : stage.getResults()) {
                        int points = result.getPoints();
                        LocalTime finishTime = result.getCheckpointTimes()[result.getCheckpointTimes().length - 1];
                        // Add the points and finish time to the list
                        mountainPointsAndTimes.add(new AbstractMap.SimpleEntry<>(points, finishTime));
                        }
                    }
                    break;
                }
        
            if (found) break;
        }
        // If the stage is not found, throw an exception
        if (!found) {
            throw new IDNotRecognisedException("The provided stage ID does not match any stage in the system.");
        }

        // Sort the list by finish time
        mountainPointsAndTimes.sort(Comparator.comparing(AbstractMap.SimpleEntry::getValue));

        // Map the sorted list to an array of mountain points and return
        return mountainPointsAndTimes.stream().mapToInt(AbstractMap.SimpleEntry::getKey).toArray();
    }
    
    @Override
    public void eraseCyclingPortal() {
        // Clear the lists of teams and races
        teams.clear();
        races.clear();
        
        // Reset the counters
        teamCounter = 0;
        raceCounter = 0;  
    }
        
    
    @Override
    public void saveCyclingPortal(String filename) throws IOException {
        try (
            // Create a FileOutputStream to write to the specified file
            FileOutputStream fileOut = new FileOutputStream(filename);
            // Create an ObjectOutputStream to write objects to the FileOutputStream
            ObjectOutputStream out = new ObjectOutputStream(fileOut)
        ) {
            // Write the current state of the portal to the ObjectOutputStream
            out.writeObject(this);
        } catch (IOException i) {
            // Rethrow the IOException with a custom message
            throw new IOException("Error occurred while saving the portal", i);
        }
    }

   
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
                Duration elapsedTime = Duration.between(checkpointTimes[0], checkpointTimes[checkpointTimes.length - 1]);

                // Get the previous total elapsed time for the rider, or 0 if none exists
                LocalTime previousTime = riderTimes.getOrDefault(riderId, LocalTime.of(0, 0));

                // Add the elapsed time for the stage to the total elapsed time for the rider
                riderTimes.put(riderId, previousTime.plusHours(elapsedTime.toHours())
                                                     .plusMinutes(elapsedTime.toMinutesPart())
                                                     .plusSeconds(elapsedTime.toSecondsPart()));
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

        if (!found) {
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

