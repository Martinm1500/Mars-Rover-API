package com.martinm1500.marsrover.services;


import com.martinm1500.marsrover.models.Obstacle;
import com.martinm1500.marsrover.models.Rover;
import com.martinm1500.marsrover.exceptions.RoverNotFoundException;
import com.martinm1500.marsrover.exceptions.InvalidCommandException;
import com.martinm1500.marsrover.exceptions.MapNotFoundException;
import com.martinm1500.marsrover.exceptions.InvalidCoordinatesException;
import com.martinm1500.marsrover.exceptions.InvalidOrientationException;
import com.martinm1500.marsrover.exceptions.InvalidOperationException;

import java.util.List;

/**
 * Service interface for managing rovers on a map.
 */
public interface RoverService {

    /**
     * Creates a rover on a map.
     *
     * @param rover  The rover to create.
     * @param mapId  The ID of the map on which the rover should be created.
     * @return The created rover.
     * @throws MapNotFoundException         If the map with the specified ID is not found.
     * @throws InvalidOperationException    If the map already has a rover
     * @throws InvalidCoordinatesException  If the coordinates of the rover do not represent a valid position on the map.
     * @throws InvalidCoordinatesException  If the coordinates coincide with the position of an obstacle.
     * @throws InvalidOrientationException     if the orientation of the rover do not represent a valid orientation
     */
    Rover createRover(Rover rover, Long mapId) throws MapNotFoundException, InvalidCoordinatesException, InvalidOrientationException;

    /**
     * Deletes a rover.
     *
     * @param roverId The ID of the rover to delete.
     * @throws RoverNotFoundException  If the rover to be deleted is not found.
     */
    void deleteRover(Long roverId) throws RoverNotFoundException;

    /**
     * Updates the position and orientation of a rover.
     * @return The updated rover.
     * @param rover The rover with the updated position and orientation.
     * @throws RoverNotFoundException     If the rover is not found.
     * @throws InvalidCoordinatesException  If the coordinates of the rover do not represent a valid position on the map.
     * @throws InvalidCoordinatesException  If the coordinates coincide with the position of an obstacle.
     * @throws InvalidOrientationException if the orientation of rover do not represent a valid orientation
     */
    Rover updateRover(Rover rover) throws RoverNotFoundException, InvalidCoordinatesException, InvalidOrientationException;

    /**
     * Gets a rover with the specified ID.
     *
     * @param roverId The ID of the rover to get.
     * @return The found rover.
     * @throws RoverNotFoundException  If the rover does not exist.
     */
    Rover getRover(Long roverId) throws RoverNotFoundException;

    /**
     * Gets a rover associated with a specific map.
     *
     * @param mapId The ID of the map.
     * @return The rover associated with the map.
     * @throws MapNotFoundException If the map with the provided ID does not exist or if there is no rover associated with the map.
     */
    Rover getRoverByMapId(Long mapId) throws MapNotFoundException;

    /**
     * Executes the commands provided on a rover with the specified ID.
     *
     * @param roverId  The ID of the rover on which commands will be executed.
     * @param commands The list of commands to be executed.
     * @return The obstacle encountered, or null if the sequence completes successfully.
     * @throws RoverNotFoundException  If the provided ID does not belong to any existing rover.
     * @throws InvalidCommandException If any command in the list is other than: 'r', 'b', 'f', 'l'.
     */
    Obstacle executeCommands(Long roverId, List<Character> commands) throws RoverNotFoundException, InvalidCommandException;
}
