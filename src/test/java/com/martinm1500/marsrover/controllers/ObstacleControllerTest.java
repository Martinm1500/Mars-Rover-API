package com.martinm1500.marsrover.controllers;

import com.martinm1500.marsrover.dtos.ObstacleDTO;
import com.martinm1500.marsrover.exceptions.InvalidCoordinatesException;
import com.martinm1500.marsrover.exceptions.MapNotFoundException;
import com.martinm1500.marsrover.exceptions.ObstacleNotFoundException;
import com.martinm1500.marsrover.models.Obstacle;
import com.martinm1500.marsrover.services.ObstacleServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ObstacleControllerTest {

    @Mock
    private ObstacleServiceImpl obstacleService;

    @InjectMocks
    private ObstacleController obstacleController;

    @Test
    @DisplayName("Create Obstacle Successfully")
    void testCreateObstacleSuccessfully() {
        // Arrange
        Long mapId = 1L;
        Obstacle obstacleToCreate = new Obstacle(2,3);

        ObstacleDTO obstacleDTO = ObstacleDTO.convertToDTO(obstacleToCreate);

        // Expected service behavior
        when(obstacleService.createObstacle(obstacleToCreate, mapId)).thenReturn(obstacleToCreate);

        // Act
        ResponseEntity<?> response = obstacleController.createObstacle(obstacleToCreate, mapId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(obstacleDTO, response.getBody());
    }

    @Test
    @DisplayName("Create Obstacle - MapNotFoundException")
    void testCreateObstacleMapNotFoundException() {
        // Arrange
        Long mapId = 1L;
        Obstacle obstacleToCreate = new Obstacle(2,4);
        String errorMessage = "Map with ID: " + mapId + " not found";

        // Expected service behavior
        when(obstacleService.createObstacle(obstacleToCreate, mapId)).thenThrow(new MapNotFoundException(errorMessage));

        // Act
        ResponseEntity<?> response = obstacleController.createObstacle(obstacleToCreate, mapId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    @DisplayName("Create Obstacle - InvalidCoordinatesException")
    void testCreateObstacleInvalidCoordinatesException() {
        // Arrange
        Long mapId = 1L;  //ID of a valid map with dimensions 8x8
        Obstacle obstacleToCreate = new Obstacle(4,10);
        String errorMessage = "The obstacle does not have a valid position on the map. Coordinates: ("
                + obstacleToCreate.getX() + ", " + obstacleToCreate.getY() + ")";

        // Expected service behavior
        when(obstacleService.createObstacle(obstacleToCreate, mapId)).thenThrow(new InvalidCoordinatesException(errorMessage));

        // Act
        ResponseEntity<?> response = obstacleController.createObstacle(obstacleToCreate, mapId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Delete Obstacle Successfully")
    void testDeleteObstacleSuccessfully() {
        // Arrange
        Long obstacleId = 1L;

        // Expected service behavior
        doNothing().when(obstacleService).deleteObstacle(obstacleId);

        // Act
        ResponseEntity<?> response = obstacleController.deleteObstacle(obstacleId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Obstacle with ID: " + obstacleId + " was deleted successfully", response.getBody());
    }

    @Test
    @DisplayName("Delete Obstacle - ObstacleNotFoundException")
    void testDeleteObstacleObstacleNotFoundException() {
        // Arrange
        Long obstacleId = 1L;
        String errorMessage = "Could not find obstacle with ID: " + obstacleId;

        // Expected service behavior
        doThrow(new ObstacleNotFoundException(errorMessage)).when(obstacleService).deleteObstacle(obstacleId);

        // Act
        ResponseEntity<?> response = obstacleController.deleteObstacle(obstacleId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    //------------------------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Get All Obstacles From Map Successfully")
    void testGetAllObstaclesFromMapSuccessfully() {
        // Arrange
        Long mapId = 1L;
        List<Obstacle> obstacles = Arrays.asList(new Obstacle(2,3), new Obstacle(5,7));

        List<ObstacleDTO> obstacleDTOs = obstacles.stream()
                .map(ObstacleDTO::convertToDTO)
                .collect(Collectors.toList());

        // Expected service behavior
        when(obstacleService.getAllFromMap(mapId)).thenReturn(obstacles);

        // Act
        ResponseEntity<?> response = obstacleController.getAllObstaclesFromMap(mapId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(obstacleDTOs,response.getBody());
    }

    @Test
    @DisplayName("Get All Obstacles From Map - MapNotFoundException")
    void testGetAllObstaclesFromMapMapNotFoundException() {
        // Arrange
        Long mapId = 1L;
        String errorMessage = "Could not find map with ID: " + mapId;

        // Expected service behavior
        when(obstacleService.getAllFromMap(mapId)).thenThrow(new MapNotFoundException(errorMessage));

        // Act
        ResponseEntity<?> response = obstacleController.getAllObstaclesFromMap(mapId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
}
