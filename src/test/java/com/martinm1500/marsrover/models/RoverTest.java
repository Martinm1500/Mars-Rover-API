package com.martinm1500.marsrover.models;


import com.martinm1500.marsrover.exceptions.InvalidCommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoverTest {

    private Rover rover;
    private Map map;

    @BeforeEach
    public void setUp() {
        // Initialize the rover object for testing
        map = new Map(6, 6,"Tierra");
        rover = new Rover(4, 4, Rover.NORTH);
        rover.setMap(map);
    }

    @Test
    public void executeCommands_validCommandsNoObstacle_returnNull() {

        //tests the correct operation of moveForward (f) together with turnRight(r) and turnLeft(l)
        rover.executeCommands(List.of('f', 'f', 'f', 'f'));
        assertEquals(new Rover(1, 1, Rover.SOUTH), rover);
        assertNull(rover.getReportedObstacle());

        rover.executeCommands(List.of('f', 'f', 'f', 'f'));
        assertEquals(new Rover(1, 5, Rover.SOUTH), rover);
        assertNull(rover.getReportedObstacle());

        rover.executeCommands(List.of('f', 'f', 'f', 'f'));
        assertEquals(new Rover(4, 4, Rover.NORTH), rover);
        assertNull(rover.getReportedObstacle());

        rover.executeCommands(List.of('r', 'f', 'f', 'f'));
        assertEquals(new Rover(1, 4, Rover.EAST), rover);
        assertNull(rover.getReportedObstacle());

        rover.executeCommands(List.of('r', 'r', 'f', 'f'));
        assertEquals(new Rover(5, 4, Rover.WEST), rover);
        assertNull(rover.getReportedObstacle());

        rover.executeCommands(List.of('l', 'r', 'f', 'f'));
        assertEquals(new Rover(3, 4, Rover.WEST), rover);
        assertNull(rover.getReportedObstacle());

        rover.executeCommands(List.of('l', 'f', 'f', 'f'));
        assertEquals(new Rover(6, 6, Rover.NORTH), rover);
        assertNull(rover.getReportedObstacle());

        //tests the correct operation of moveBackward (f) together with turnRight(r) and turnLeft(l)
        //current position of the rover: (6,6)

        rover.executeCommands(List.of('b', 'b', 'b', 'r'));
        assertEquals(new Rover(3, 4, Rover.WEST), rover);
        assertNull(rover.getReportedObstacle());

        rover.executeCommands(List.of('b', 'b', 'l', 'r'));
        assertEquals(new Rover(5, 4, Rover.WEST), rover);
        assertNull(rover.getReportedObstacle());

        rover.executeCommands(List.of('b', 'b', 'l', 'l'));
        assertEquals(new Rover(1, 4, Rover.EAST), rover);
        assertNull(rover.getReportedObstacle());

        rover.executeCommands(List.of('b', 'b', 'b', 'l'));
        assertEquals(new Rover(4, 4, Rover.NORTH), rover);
        assertNull(rover.getReportedObstacle());

        rover.executeCommands(List.of('b', 'b', 'b', 'b'));
        assertEquals(new Rover(1, 5, Rover.SOUTH), rover);
        assertNull(rover.getReportedObstacle());

        rover.executeCommands(List.of('b', 'b', 'b', 'b'));
        assertEquals(new Rover(1, 1, Rover.SOUTH), rover);
        assertNull(rover.getReportedObstacle());

        rover.executeCommands(List.of('b', 'b', 'b', 'b'));
        assertEquals(new Rover(4, 4, Rover.NORTH), rover);
        assertNull(rover.getReportedObstacle());
    }

    @Test
    public void executeCommands_validCommandsWithObstacle_returnObstacle() {
        // Mocking an obstacle at position (4, 1) on the map
        Obstacle obstacle = new Obstacle(4,1);
        map.addObstacle(obstacle);

        // Try to move the rover towards the obstacle (4, 1)
        //current position of rover (4,4,NORTH)
        Obstacle reportedObstacle = rover.executeCommands(List.of('f', 'f', 'f'));

        assertNotNull(reportedObstacle);
        assertEquals(new Obstacle(4,1), reportedObstacle);

        //rover stops at (4,2)
        assertEquals(new Rover(4,2,Rover.NORTH),rover);
    }

    @Test
    public void executeCommands_invalidCommand_throwException() {
        // Test the executeCommands method with an invalid command and check if it throws an IllegalCommandException
        assertThrows(InvalidCommandException.class, () -> rover.executeCommands(List.of('f', 'x', 'b')));
        //"x" does not represent a valid command
    }

    @Test
    public void isValidCommand_validCommand_returnTrue() {
        // Test isValidCommand with a valid command and ensure it returns true
        boolean result = Rover.isValidCommand('f') && Rover.isValidCommand('b') && Rover.isValidCommand('r') && Rover.isValidCommand('l');
        assertTrue(result);
    }

    @Test
    public void isValidCommand_invalidCommand_returnFalse() {
        // Test isValidCommand with an invalid command and ensure it returns false
        boolean result = Rover.isValidCommand('F');
        assertFalse(result);
    }


    //validations are tested

    @Test
    public void isValidOrientation_validOrientation_returnTrue() {
        // Test isValidOrientation with a valid orientation and ensure it returns true
        boolean result = Rover.isValidOrientation('N') && Rover.isValidOrientation('S')
                && Rover.isValidOrientation('W') && Rover.isValidOrientation('E');
        assertTrue(result);
    }

    @Test
    public void isValidOrientation_invalidOrientation_returnFalse() {
        // Test isValidOrientation with an invalid orientation and ensure it returns false
        boolean result = Rover.isValidOrientation('s');
        assertFalse(result);
    }

    @Test
    public void isValidPosition_validPosition_returnTrue() {
        // Test isValidPosition with a valid position and ensure it returns true
        boolean result = Rover.isValidPosition(3, 4, new Map(10, 10,"Mercurio"));
        // Assuming Map constructor is properly defined
        assertTrue(result);
    }

    @Test
    public void isValidPosition_invalidPosition_returnFalse() {
        // Test isValidPosition with an invalid position and ensure it returns false
        boolean result = Rover.isValidPosition(15, 20, new Map(10, 10,"Mercurio"));
        // Assuming Map constructor is properly defined
        assertFalse(result);
    }


    //test geolocation

    @Test
    public void onMeridianLeftSide_roverAtLeftSide_returnTrue() {
        Rover rover = new Rover(2, 3, Rover.NORTH);
        rover.setMap(map);
        assertTrue(rover.onMeridianLeftSide());
    }

    @Test
    public void onMeridianLeftSide_roverNotAtLeftSide_returnFalse() {
        Rover rover = new Rover(4, 3, Rover.NORTH);
        rover.setMap(map);
        assertFalse(rover.onMeridianLeftSide());
    }

    @Test
    public void onMeridianRightSide_roverAtRightSide_returnTrue() {
        Rover rover = new Rover(4, 3, Rover.NORTH);
        rover.setMap(map);
        assertTrue(rover.onMeridianRightSide());
    }

    @Test
    public void onMeridianRightSide_roverNotAtRightSide_returnFalse() {
        Rover rover = new Rover(2, 3, Rover.NORTH);
        rover.setMap(map);
        assertFalse(rover.onMeridianRightSide());
    }

    @Test
    public void onNorthPole_roverAtNorthPole_returnTrue() {
        Rover rover = new Rover(3, 1, Rover.NORTH);
        rover.setMap(map);
        assertTrue(rover.onNorthPole());
    }

    @Test
    public void onNorthPole_roverNotAtNorthPole_returnFalse() {
        Rover rover = new Rover(3, 3, Rover.NORTH);
        rover.setMap(map);
        assertFalse(rover.onNorthPole());
    }

    @Test
    public void onSouthPole_roverAtSouthPole_returnTrue() {
        Rover rover = new Rover(3, 6, Rover.NORTH);
        rover.setMap(map);
        assertTrue(rover.onSouthPole());
    }

    @Test
    public void onSouthPole_roverNotAtSouthPole_returnFalse() {
        Rover rover = new Rover(3, 4, Rover.NORTH);
        rover.setMap(map);
        assertFalse(rover.onSouthPole());
    }
}
