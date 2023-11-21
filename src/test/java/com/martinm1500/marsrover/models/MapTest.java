package com.martinm1500.marsrover.models;

import com.martinm1500.marsrover.exceptions.InvalidCoordinatesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MapTest {

    @Test
    @DisplayName("Successfully remove an obstacle from the map.")
    void testRemoveObstacleSuccessfully() {
        // Arrange
        Map map = new Map(10, 10, "Mars");
        Obstacle obstacle = new Obstacle(3, 3);
        map.addObstacle(obstacle);

        // Act
        map.removeObstacle(obstacle);

        // Assert
        assertFalse(map.getObstacles().contains(obstacle));
        assertNull(obstacle.getMap());
    }

    @Test
    @DisplayName("Successfully add an obstacle to the map.")
    void testAddObstacleSuccessfully() {
        // Arrange
        Map map = new Map(10, 10, "Mars");
        Obstacle obstacle = new Obstacle(3, 3);

        // Act
        map.addObstacle(obstacle);

        // Assert
        assertTrue(map.getObstacles().contains(obstacle));
        assertEquals(map, obstacle.getMap());
    }

    @Test
    @DisplayName("Check if dimensions are valid.")
    void testIsValidDimensions() {
        assertTrue(Map.isValidDimensions(6, 6));
        assertFalse(Map.isValidDimensions(7, 6));
        assertFalse(Map.isValidDimensions(6, 7));
        assertFalse(Map.isValidDimensions(5, 6));
        assertFalse(Map.isValidDimensions(6, 5));
        assertFalse(Map.isValidDimensions(0, 6));
        assertFalse(Map.isValidDimensions(6, 0));
    }

    @Test
    @DisplayName("Check if a position is occupied by obstacles.")
    void testIsPositionOccupied() {
        // Arrange
        Map map = new Map(10, 10, "Mars");
        Obstacle obstacle = new Obstacle(3, 3);
        map.addObstacle(obstacle);

        // Assert
        assertTrue(map.isPositionOccupied(3, 3));
        assertFalse(map.isPositionOccupied(1, 1));
    }

    @Test
    @DisplayName("Check if a position is valid for adding an obstacle.")
    void testValidPositionOfObstacle() {
        // Arrange
        Map map = new Map(10, 10, "Mars");
        Obstacle obstacle = new Obstacle(3, 3);

        // Assert
        assertTrue(map.validPositionOfObstacle(obstacle));
        assertFalse(map.validPositionOfObstacle(new Obstacle(0, 3)));
        assertFalse(map.validPositionOfObstacle(new Obstacle(3, 0)));
        assertFalse(map.validPositionOfObstacle(new Obstacle(11, 3)));
        assertFalse(map.validPositionOfObstacle(new Obstacle(3, 11)));
    }

    @Test
    @DisplayName("Successfully get an obstacle at a specific position.")
    void testGetObstacle() {
        // Arrange
        Map map = new Map(10, 10, "Mars");
        Obstacle obstacle = new Obstacle(3, 3);
        map.addObstacle(obstacle);

        // Act
        Obstacle result = map.getObstacle(3, 3);

        // Assert
        assertEquals(obstacle, result);
    }

    @Test
    @DisplayName("Return null when getting an obstacle at a non-existing position.")
    void testGetNonExistingObstacle() {
        // Arrange
        Map map = new Map(10, 10, "Mars");

        // Act
        Obstacle result = map.getObstacle(3, 3);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Attempt to remove a non-existing obstacle from the map.")
    void testRemoveNonExistingObstacle() {
        // Arrange
        Map map = new Map(10, 10, "Mars");
        Obstacle nonExistingObstacle = new Obstacle(3, 3);

        // Act
        map.removeObstacle(nonExistingObstacle);

        // Assert
        assertFalse(map.getObstacles().contains(nonExistingObstacle));
    }

    @Test
    @DisplayName("Attempt to add an obstacle to an invalid position on the map.")
    void testAddObstacleToInvalidPosition() {
        // Arrange
        Map map = new Map(10, 10, "Mars");
        Obstacle invalidObstacle = new Obstacle(11, 11);

        // Act and Assert
        assertThrows(InvalidCoordinatesException.class, () -> map.addObstacle(invalidObstacle));
    }

    @Test
    @DisplayName("Attempt to add an obstacle to the map with null coordinates.")
    void testAddObstacleWithNullCoordinates() {
        // Arrange
        Map map = new Map(10, 10, "Mars");
        Obstacle obstacleWithNullCoordinates = new Obstacle(0, 0);

        // Act and Assert
        assertThrows(InvalidCoordinatesException.class, () -> map.addObstacle(obstacleWithNullCoordinates));
    }

    @Test
    @DisplayName("Attempt to get an obstacle at negative coordinates.")
    void testGetObstacleAtNegativeCoordinates() {
        // Arrange
        Map map = new Map(10, 10, "Mars");

        // Act and Assert
        assertNull(map.getObstacle(-1, -1));
    }
}
