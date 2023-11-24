package com.martinm1500.marsrover.controllers;

import com.martinm1500.marsrover.dtos.MapDTO;
import com.martinm1500.marsrover.dtos.MapFullDTO;
import com.martinm1500.marsrover.exceptions.InvalidMapDimensionsException;
import com.martinm1500.marsrover.exceptions.MapNotFoundException;
import com.martinm1500.marsrover.models.Map;
import com.martinm1500.marsrover.models.Obstacle;
import com.martinm1500.marsrover.models.Rover;
import com.martinm1500.marsrover.services.MapServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MapControllerTest {

    @Mock
    private MapServiceImpl mapService;

    @InjectMocks
    private MapController mapController;

    @Test
    @DisplayName("Create Map Successfully")
    void testCreateMapSuccessfully() {
        // Arrange
        Long mapId = 1L;
        Map mapToCreate = new Map(12,12,"Jupiter");
        mapToCreate.setId(mapId);

        MapDTO mapDTO = MapDTO.convertToDTO(mapToCreate);

        // Expected service behavior
        when(mapService.createMap(mapToCreate)).thenReturn(mapToCreate);

        // Act
        ResponseEntity<?> response = mapController.createMap(mapToCreate);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mapDTO,response.getBody());
    }

    @Test
    @DisplayName("Create Map - Invalid Map Dimensions")
    void testCreateMapInvalidDimensions() {
        // Arrange
        Map invalidMap = new Map(8,9,"Asteroid");
        String errorMessage = "Only positive even dimensions are allowed.";

        // Expected service behavior
        when(mapService.createMap(invalidMap)).thenThrow(new InvalidMapDimensionsException(errorMessage));

        // Act
        ResponseEntity<?> response = mapController.createMap(invalidMap);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Delete Map Successfully")
    void testDeleteMapSuccessfully() {
        // Arrange
        Long mapId = 1L;

        // Expected service behavior
        doNothing().when(mapService).deleteMap(mapId);

        // Act
        ResponseEntity<?> response = mapController.deleteMap(mapId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Map with ID: " + mapId + " was deleted successfully", response.getBody());
    }

    @Test
    @DisplayName("Delete Map - MapNotFoundException")
    void testDeleteMapMapNotFoundException() {
        // Arrange
        Long mapId = 1L;
        String errorMessage = "Map with ID: " + mapId + " not found";

        // Expected service behavior
        doThrow(new MapNotFoundException(errorMessage)).when(mapService).deleteMap(mapId);

        // Act
        ResponseEntity<?> response = mapController.deleteMap(mapId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Get Map Successfully")
    void testGetMapSuccessfully() {
        // Arrange
        Long mapId = 1L;
        Map obtainedMap = new Map(12,12,"Jupiter");
        obtainedMap.setRover(new Rover(4,4,Rover.NORTH));
        obtainedMap.setObstacles(Arrays.asList(new Obstacle(3,3),new Obstacle(6,3)));

        MapFullDTO mapFullDTO = MapFullDTO.convertToFullDTO(obtainedMap);

        // Expected service behavior
        when(mapService.getMap(mapId)).thenReturn(obtainedMap);

        // Act
        ResponseEntity<?> response = mapController.getMap(mapId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mapFullDTO, response.getBody());

    }

    @Test
    @DisplayName("Get Map - MapNotFoundException")
    void testGetMapMapNotFoundException() {
        // Arrange
        Long mapId = 1L;
        String errorMessage = "Map with ID: " + mapId + " not found";

        // Expected service behavior
        when(mapService.getMap(mapId)).thenThrow(new MapNotFoundException(errorMessage));

        // Act
        ResponseEntity<?> response = mapController.getMap(mapId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Get All Maps Successfully")
    void testGetAllMapsSuccessfully() {
        // Arrange
        List<Map> maps = Arrays.asList(new Map(8,8,"Mars"), new Map(12,12,"Jupiter"));

        // Expected service behavior
        when(mapService.getAllMaps()).thenReturn(maps);

        // Act
        ResponseEntity<List<MapDTO>> response = mapController.getAllMaps();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<MapDTO> obtainedMapDTOs = response.getBody();
        assertEquals(maps.size(), obtainedMapDTOs.size());
    }
}
