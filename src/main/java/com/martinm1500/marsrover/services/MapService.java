package com.martinm1500.marsrover.services;

import com.martinm1500.marsrover.models.Map;
import com.martinm1500.marsrover.exceptions.MapNotFoundException;
import com.martinm1500.marsrover.exceptions.InvalidMapDimensionsException;

import java.util.List;

/**
 * Service interface for managing maps.
 */
public interface MapService {

    /**
     * Creates a map with positive even dimensions.
     *
     * @param map The map to be added.
     * @return The added map.
     * @throws InvalidMapDimensionsException If the dimensions are not positive and even.
     */
    Map createMap(Map map) throws InvalidMapDimensionsException;

    /**
     * Deletes a map with the specified ID.
     *
     * @param id The ID of the map to be removed.
     * @throws MapNotFoundException If the provided ID does not match any existing map.
     */
    void deleteMap(Long id) throws MapNotFoundException;

    /**
     * Retrieves a map with the specified ID.
     *
     * @param id The ID of the map to be retrieved.
     * @return The retrieved map.
     * @throws MapNotFoundException If the provided ID does not match any existing map.
     */
    Map getMap(Long id) throws MapNotFoundException;

    /**
     * Retrieves all existing maps.
     *
     * @return List of all maps.
     */
    List<Map> getAllMaps();
}
