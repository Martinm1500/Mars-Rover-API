package com.martinm1500.marsrover.controllers;

import com.martinm1500.marsrover.services.RoverServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoverControllerTest {

    @InjectMocks
    private RoverController roverController;

    @Mock
    private RoverServiceImpl roverService;

    @Test
    @DisplayName("Create Rover Successfully")
    void testCreateRover() {
        // Check that the response status is HttpStatus.CREATED (201)
    }

    @Test
    @DisplayName("Create Rover - MapNotFoundException")
    void testCreateRoverMapNotFoundException() {
        // Check that the response status is HttpStatus.NOT_FOUND (404)
    }

    @Test
    @DisplayName("Create Rover - InvalidCoordinatesException(Position Not on Map)")
    void testCreateRoverInvalidCoordinatesExceptionPositionNotOnMap() {
        // Check that the response status is HttpStatus.BAD_REQUEST (400)
    }

    @Test
    @DisplayName("Create Rover - InvalidCoordinatesException (Position Occupied by Obstacle)")
    void testCreateRoverInvalidCoordinatesExceptionPositionOccupiedByObstacle() {
        // Check that the response status is HttpStatus.BAD_REQUEST (400)
    }
    @Test
    @DisplayName("Create Rover - InvalidOrientationException")
    void testCreateRoverInvalidOrientationException() {
        // Check that the response status is HttpStatus.BAD_REQUEST (400)
    }

    @Test
    @DisplayName("Create Rover - InvalidOperationException (Map already has a rover)")
    void testCreateRoverInvalidOperationException() {
        // Check that the response status is HttpStatus.BAD_REQUEST (400)
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Delete Rover Successfully")
    void testDeleteRover() {
        // Check that the response status is HttpStatus.OK (200)
    }
    @Test
    @DisplayName("Delete Rover - RoverNotFoundException")
    void testDeleteRoverRoverNotFoundException() {
        // Check that the response status is HttpStatus.NOT_FOUND (404)
    }

    //------------------------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Update Rover Successfully")
    void testUpdateRover() {
        // Check that the response status is HttpStatus.OK (200)
    }

    @Test
    @DisplayName("Update Rover - InvalidCoordinatesException (Position Not on Map)")
    void testUpdateRoverInvalidCoordinatesExceptionPositionNotOnMap() {
        // Check that the response status is HttpStatus.BAD_REQUEST (400)
    }

    @Test
    @DisplayName("Update Rover - InvalidCoordinatesException (Position Occupied by Obstacle)")
    void testUpdateRoverInvalidCoordinatesExceptionPositionOccupiedByObstacle() {
        // Check that the response status is HttpStatus.BAD_REQUEST (400)
    }

    @Test
    @DisplayName("Update Rover - InvalidOrientationException")
    void testUpdateRoverInvalidOrientationException() {
        // Check that the response status is HttpStatus.BAD_REQUEST (400)

    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Get Rover Successfully")
    void testGetRover() {
        // Check that the response status is HttpStatus.OK (200)
    }

    @Test
    @DisplayName("Get Rover - RoverNotFoundException")
    void testGetRoverRoverNotFoundException() {
    }

    //------------------------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Get Rover By Map ID Successfully")
    void testGetRoverByMapId() {
        // Check that the response status is HttpStatus.OK (200)
    }

    @Test
    @DisplayName("Get Rover By Map ID - MapNotFoundException")
    void testGetRoverByMapIdMapNotFoundException() {
    }
    //------------------------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Execute Commands Successfully (No Obstacle)")
    void testExecuteCommandsNoObstacle() {
        // Ensure that the RoverController.executeCommands(roverId, commands) method returns HttpStatus.OK (200)
        // Check that the response status is HttpStatus.OK (200) if no obstacle is encountered
    }

    @Test
    @DisplayName("Execute Commands with Obstacle (Conflict)")
    void testExecuteCommandsWithObstacle() {
        // Check that the response status is HttpStatus.CONFLICT (409) if an obstacle is encountered
        // Check that the response body contains information about the obstacle
    }

    @Test
    @DisplayName("Execute Commands - RoverNotFoundException")
    void testExecuteCommandsRoverNotFoundException() {
        // Check that the response status is HttpStatus.NOT_FOUND (404)
        // Check that the response body contains information about the RoverNotFoundException
    }

    @Test
    @DisplayName("Execute Commands - InvalidCommandException")
    void testExecuteCommandsInvalidCommandException() {
        // Check that the response status is HttpStatus.BAD_REQUEST (400)
        // Check that the response body contains information about the InvalidCommandException
    }
}
