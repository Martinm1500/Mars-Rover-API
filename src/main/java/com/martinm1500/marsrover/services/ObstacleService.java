package com.martinm1500.marsrover.services;

import com.martinm1500.marsrover.models.Obstacle;
import com.martinm1500.marsrover.exceptions.MapNotFoundException;
import com.martinm1500.marsrover.exceptions.ObstacleNotFoundException;
import com.martinm1500.marsrover.exceptions.InvalidCoordinatesException;
import java.util.List;

/**
 * Service interface for managing obstacles on a map.
 */
public interface ObstacleService {

    /**
     * Create an obstacle to a map.
     *
     * @param obstacle The obstacle to create.
     * @param mapId    The ID of the map to which the obstacle should be created.
     * @return The added obstacle.
     * @throws MapNotFoundException         If the map is not found.
     * @throws InvalidCoordinatesException      If the coordinates of the obstacle do not represent a valid position on the map.
     */
    Obstacle createObstacle(Obstacle obstacle, Long mapId) throws MapNotFoundException, InvalidCoordinatesException;

    /**
     * Deletes an obstacle with the specified ID.
     *
     * @param obstacleId The ID of the obstacle to delete.
     * @throws ObstacleNotFoundException     If the obstacle is not found.
     */
    void deleteObstacle(Long obstacleId) throws ObstacleNotFoundException;

    /**
     * Gets an obstacle with the specified ID.
     *
     * @param obstacleId The ID of the obstacle to get.
     * @return The found obstacle.
     * @throws ObstacleNotFoundException If the obstacle is not found.
     */
    Obstacle getObstacle(Long obstacleId) throws ObstacleNotFoundException;

    /**
     * Returns all obstacles that are on a map.
     *
     * @param mapId The ID of the map.
     * @return List of obstacles on the map.
     * @throws MapNotFoundException If the map does not exist.
     */
    List<Obstacle> getAllFromMap(Long mapId) throws MapNotFoundException;
}

