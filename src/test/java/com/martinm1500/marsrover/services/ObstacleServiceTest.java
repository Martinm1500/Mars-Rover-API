package com.martinm1500.marsrover.services;

import com.martinm1500.marsrover.exceptions.InvalidCoordinatesException;
import com.martinm1500.marsrover.exceptions.MapNotFoundException;
import com.martinm1500.marsrover.exceptions.ObstacleNotFoundException;
import com.martinm1500.marsrover.models.Map;
import com.martinm1500.marsrover.models.Obstacle;
import com.martinm1500.marsrover.repositories.MapRepository;
import com.martinm1500.marsrover.repositories.ObstacleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ObstacleServiceTest {

    @Mock
    private MapRepository mapRepository;

    @Mock
    private ObstacleRepository obstacleRepository;

    @InjectMocks
    private ObstacleServiceImpl obstacleService;

    @Test
    @DisplayName("Successfully add an obstacle to a map")
    void testAddObstacleSuccessfully() {
        // Arrange
        Long mapId = 1L;
        Map map = new Map(12, 12, "Jupiter");
        Obstacle newObstacle = new Obstacle(3, 3);

        // Expected repositories behavior
        when(mapRepository.findById(mapId)).thenReturn(Optional.of(map));
        when(obstacleRepository.save(newObstacle)).thenReturn(newObstacle);

        // Act
        Obstacle addedObstacle = obstacleService.createObstacle(newObstacle, mapId);

        // Assert
        verify(mapRepository, times(1)).save(eq(map));
        verify(obstacleRepository, times(1)).save(newObstacle);
        assertTrue(map.getObstacles().contains(addedObstacle));
        assertEquals(addedObstacle.getMap(), map);
    }

    @Test
    @DisplayName("Attempt to add an obstacle to a non-existing map, expect MapNotFoundException")
    void testAddObstacleToNonExistingMap() {
        //Arrange
        Long mapID = 1L;
        Obstacle newObstacle = new Obstacle(4,4);

        //expected repositories behavior
        when(mapRepository.findById(mapID)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(MapNotFoundException.class, () -> obstacleService.createObstacle(newObstacle,mapID));
        verify(mapRepository, never()).save(any());
        verify(obstacleRepository,never()).save(any());
        assertNull(newObstacle.getMap());
    }

    @Test
    @DisplayName("Attempt to add an obstacle with invalid coordinates to a map, expect InvalidCoordinatesException")
    void testAddObstacleWithInvalidCoordinates() {
        //Arrange
        Long mapID = 1L;
        Map map = new Map(12,12,"Jupiter");
        Obstacle newObstacle = new Obstacle(15,4);

        //expected repositories behavior
        when(mapRepository.findById(mapID)).thenReturn(Optional.of(map));

        //Act and Assert
        assertThrows(InvalidCoordinatesException.class, () -> obstacleService.createObstacle(newObstacle,mapID));
        verify(mapRepository, never()).save(any());
        verify(obstacleRepository,never()).save(any());
        assertNull(newObstacle.getMap());
        assertFalse(map.getObstacles().contains(newObstacle));
    }

    @Test
    @DisplayName("Successfully remove an obstacle")
    void testRemoveObstacleSuccessfully() {
        //Arrange
        Long obstacleId = 1L;
        Obstacle obstacle = new Obstacle(3,3);
        Map map = new Map(12,12,"Jupiter");
        obstacle.setMap(map);

        //expected repositories behavior
        when(obstacleRepository.findById(obstacleId)).thenReturn(Optional.of(obstacle));

        //Act
        obstacleService.deleteObstacle(obstacleId);

        //Assert
        verify(obstacleRepository,times(1)).delete(obstacle);
        verify(mapRepository,times(1)).save(map);
    }

    @Test
    @DisplayName("Attempt to remove a non-existing obstacle, expect ObstacleNotFoundException")
    void testRemoveNonExistingObstacle() {
        //Arrange
        Long obstacleId = 1L;

        //expected repositories behavior
        when(obstacleRepository.findById(obstacleId)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(ObstacleNotFoundException.class, () -> obstacleService.deleteObstacle(obstacleId));
        verify(obstacleRepository,never()).deleteById(obstacleId);
        verify(mapRepository,never()).save(any());
    }

    @Test
    @DisplayName("Successfully retrieve an obstacle")
    void testGetObstacleSuccessfully() {
        //Arrange
        Long obstacleId = 1L;
        Obstacle obstacle = new Obstacle(4,4);

        //Expected repository behavior
        when(obstacleRepository.findById(obstacleId)).thenReturn(Optional.of(obstacle));

        //Act
        Obstacle obtainedObstacle = obstacleService.getObstacle(obstacleId);

        //Assert
        verify(obstacleRepository, times(1)).findById(obstacleId);
        assertNotNull(obtainedObstacle);
        assertSame(obstacle, obtainedObstacle);
    }

    @Test
    @DisplayName("Attempt to retrieve a non-existing obstacle, expect ObstacleNotFoundException")
    void testGetNonExistingObstacle() {
        //Arrange
        Long obstacleId = 1L;

        //Expected repository behavior
        when(obstacleRepository.findById(obstacleId)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(ObstacleNotFoundException.class, () -> obstacleService.getObstacle(obstacleId));
    }

    @Test
    @DisplayName("Successfully retrieve all obstacles from a map")
    void testGetAllObstaclesSuccessfully() {
        // Arrange
        Long mapId = 1L;
        List<Obstacle> obstacles = Arrays.asList(new Obstacle(3, 3), new Obstacle(5, 5));

        //Expected repository behavior
        when(mapRepository.existsById(mapId)).thenReturn(true);
        when(obstacleRepository.findByMapId(mapId)).thenReturn(obstacles);

        // Act
        List<Obstacle> obtainedObstacles = obstacleService.getAllFromMap(mapId);

        // Assert
        assertEquals(obstacles.size(), obtainedObstacles.size());
        assertTrue(obtainedObstacles.containsAll(obstacles));
    }

    @Test
    @DisplayName("Attempt to retrieve all obstacles from a non-existing map throws MapNotFoundException")
    void testGetAllObstaclesForNonExistingMapThrowsException() {
        // Arrange
        Long mapId = 1L;

        // Expected repository behavior
        when(mapRepository.existsById(mapId)).thenReturn(false);

        // Act and Assert
        assertThrows(MapNotFoundException.class, () -> obstacleService.getAllFromMap(mapId));
    }
}
