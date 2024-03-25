import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CyclingPortalImplTest {

    private CyclingPortalImpl cyclingPortal;

    @BeforeEach
    public void setUp() {
        cyclingPortal = new CyclingPortalImpl();
    }

    @Test
    public void testGetRaceIds_EmptyRaces() {
        int[] raceIds = cyclingPortal.getRaceIds();
        Assertions.assertEquals(0, raceIds.length);
    }

    @Test
    public void testGetRaceIds_NonEmptyRaces() {
        // Add some races to the portal
        cyclingPortal.createRace("Race 1", "Description 1");
        cyclingPortal.createRace("Race 2", "Description 2");

        int[] raceIds = cyclingPortal.getRaceIds();
        Assertions.assertEquals(2, raceIds.length);
        Assertions.assertEquals(0, raceIds[0]);
        Assertions.assertEquals(1, raceIds[1]);
    }

    @Test
    public void testCreateRace_ValidNameAndDescription() {
        int raceId = cyclingPortal.createRace("Race 1", "Description 1");
        Assertions.assertEquals(0, raceId);
    }

    @Test
    public void testCreateRace_DuplicateName() {
        // Add a race with the same name
        cyclingPortal.createRace("Race 1", "Description 1");

        // Try to create a race with the same name
        Assertions.assertThrows(IllegalNameException.class, () -> {
            cyclingPortal.createRace("Race 1", "Description 2");
        });
    }

    @Test
    public void testCreateRace_InvalidName_Null() {
        Assertions.assertThrows(InvalidNameException.class, () -> {
            cyclingPortal.createRace(null, "Description 1");
        });
    }

    @Test
    public void testCreateRace_InvalidName_Empty() {
        Assertions.assertThrows(InvalidNameException.class, () -> {
            cyclingPortal.createRace("", "Description 1");
        });
    }

    @Test
    public void testCreateRace_InvalidName_TooLong() {
        Assertions.assertThrows(InvalidNameException.class, () -> {
            cyclingPortal.createRace("This is a very long race name that exceeds the maximum length", "Description 1");
        });
    }

    @Test
    public void testCreateRace_InvalidName_ContainsWhitespace() {
        Assertions.assertThrows(InvalidNameException.class, () -> {
            cyclingPortal.createRace("Race 1 ", "Description 1");
        });
    }

    // Add more test cases for other methods...

}