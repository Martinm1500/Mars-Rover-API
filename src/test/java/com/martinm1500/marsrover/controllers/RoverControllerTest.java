package com.martinm1500.marsrover.controllers;

import com.martinm1500.marsrover.dtos.RoverDTO;
import com.martinm1500.marsrover.exceptions.InvalidCoordinatesException;
import com.martinm1500.marsrover.exceptions.InvalidOperationException;
import com.martinm1500.marsrover.exceptions.InvalidOrientationException;
import com.martinm1500.marsrover.exceptions.MapNotFoundException;
import com.martinm1500.marsrover.models.Rover;
import com.martinm1500.marsrover.services.RoverServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoverControllerTest {

    @InjectMocks
    private RoverController roverController;

    @Mock
    private RoverServiceImpl roverService;

    @Test
    @DisplayName("Create Rover Successfully")
    void testCreateRover() {
        // Arrange
        Long mapId = 1L;
        Rover rover = new Rover(4,4,Rover.NORTH);

        Rover createdRover = new Rover(4,4,Rover.NORTH);
        Long autogeneratedRoverId = 1L;
        createdRover.setId(autogeneratedRoverId);

        RoverDTO roverDTO = RoverDTO.convertToDTO(createdRover);
        roverDTO.setMapId(mapId);

        //Expected repository behavior
        when(roverService.createRover(eq(rover), eq(mapId))).thenReturn(createdRover);

        // Act
        ResponseEntity<?> response = roverController.createRover(mapId, rover);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(roverDTO, response.getBody());
        assertEquals(mapId, roverDTO.getMapId());
    }

    @Test
    @DisplayName("Create Rover - MapNotFoundException")
    void testCreateRoverMapNotFoundException() {
        //Arrange
        Long mapId = 73L;
        Rover rover = new Rover(4,4,Rover.NORTH);
        String errorMessage = "Could not find map with ID: 73";

        //Expected repository behavior
        when(roverService.createRover(rover,mapId)).thenThrow(new MapNotFoundException(errorMessage));

        //Act
        ResponseEntity<?> response = roverController.createRover(mapId, rover);

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    @DisplayName("Create Rover - InvalidCoordinatesException(Position Not on Map)")
    void testCreateRoverInvalidCoordinatesExceptionPositionNotOnMap() {
        // Arrange
        Long mapId = 1L;  // ID of a valid map with dimensions 8x8
        Rover rover = new Rover(15,5,Rover.NORTH);

        String errorMessage = "The rover's coordinates do not represent a valid position on the map";

        //Expected repository behavior
        when(roverService.createRover(rover,mapId)).thenThrow(new InvalidCoordinatesException(errorMessage));

        //Act
        ResponseEntity<?> response = roverController.createRover(1L, rover);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    @DisplayName("Create Rover - InvalidCoordinatesException (Position Occupied by Obstacle)")
    void testCreateRoverInvalidCoordinatesExceptionPositionOccupiedByObstacle() {
        // Arrange
        Long mapId = 1L;  // ID of a valid map (8x8) with an obstacle at coordinates (1,5)
        Rover rover = new Rover(1,5,Rover.NORTH);

        String errorMessage = "position (1,5) is occupied by an obstacle";

        //Expected repository behavior
        when(roverService.createRover(rover,mapId)).thenThrow(new InvalidCoordinatesException(errorMessage));

        //Act
        ResponseEntity<?> response = roverController.createRover(1L, rover);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
    @Test
    @DisplayName("Create Rover - InvalidOrientationException")
    void testCreateRoverInvalidOrientationException() {
        // Arrange
        Long mapId = 1L;  // ID of a valid map (8x8)
        Rover rover = new Rover(1,5,'n');

        String errorMessage = "Invalid rover orientation. Accepted values are: " +
                Rover.NORTH + ", " + Rover.SOUTH + ", " + Rover.EAST + ", or " + Rover.WEST;
        //NORTH (N), SOUTH (S), EAST (E) and WEST (W)

        //Expected repository behavior
        when(roverService.createRover(rover,mapId)).thenThrow(new InvalidOrientationException(errorMessage));

        //Act
        ResponseEntity<?> response = roverController.createRover(1L, rover);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    @DisplayName("Create Rover - InvalidOperationException (Map already has a rover)")
    void testCreateRoverInvalidOperationException() {
        // Arrange
        Long mapId = 1L;  // ID of a valid map (8x8)
        Rover rover = new Rover(1,5,'n');

        String errorMessage = "The map already has a rover" ;

        //Expected repository behavior
        when(roverService.createRover(rover,mapId)).thenThrow(new InvalidOperationException(errorMessage));

        //Act
        ResponseEntity<?> response = roverController.createRover(1L, rover);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
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
