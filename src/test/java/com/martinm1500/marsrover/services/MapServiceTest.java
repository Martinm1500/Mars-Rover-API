package com.martinm1500.marsrover.services;

import com.martinm1500.marsrover.exceptions.InvalidMapDimensionsException;
import com.martinm1500.marsrover.exceptions.MapNotFoundException;
import com.martinm1500.marsrover.models.Map;
import com.martinm1500.marsrover.repositories.MapRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class MapServiceTest {

    @Mock
    private MapRepository mapRepository;

    @InjectMocks
    private MapServiceImpl mapService;

    @Test
    @DisplayName("Successfully create a map")
    void testCreateMapSuccessfully() {
        //Arrange
        Map newMap = new Map(8,8,"Tierra");

        //expected mapRepository behavior
        when(mapRepository.save(newMap)).thenReturn(newMap);

        //act
        Map addedMap = mapService.createMap(newMap);

        //Assert
        verify(mapRepository, times(1)).save(newMap);
        assertEquals(newMap, addedMap);
    }

    @Test
    @DisplayName("Attempt to create a map with invalid dimensions, expect InvalidMapDimensionsException")
    void testCreateMapWithInvalidDimensions() {
        //Arrange
        Map invalidMap = new Map(5,8,"Invalid Map");

        //Act and Assert
        assertThrows(InvalidMapDimensionsException.class, () -> mapService.createMap(invalidMap));
        verify(mapRepository, never()).save(any());
    }

    @Test
    @DisplayName("Successfully delete a map")
    void testDeleteMapSuccessfully() {
        //Arrange
        Long mapId = 1L;

        //Expected mapRepository behavior
        when(mapRepository.existsById(mapId)).thenReturn(true);

        //Act
        mapService.deleteMap(mapId);

        //Assert
        verify(mapRepository, times(1)).deleteById(mapId);
    }

    @Test
    @DisplayName("Attempt to delete a non-existing map, expect MapNotFoundException")
    void testDeleteNonExistingMap() {
        //Arrange
        Long mapId = 1L;

        //Expected mapService behavior
        when(mapRepository.existsById(mapId)).thenReturn(false);

        //Act and Assert
        assertThrows(MapNotFoundException.class, () -> mapService.deleteMap(mapId));
        verify(mapRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Successfully retrieve a map")
    void testGetMapSuccessfully() {
        // Arrange
        Long mapId = 1L;

        // Expected mapRepository behavior

        Map expectedMap = new Map();

        when(mapRepository.findById(mapId)).thenReturn(Optional.of(expectedMap));

        // Act
        Map obtainedMap = mapService.getMap(mapId);

        // Assert
        verify(mapRepository, times(1)).findById(mapId);
        assertNotNull(obtainedMap);
        assertSame(expectedMap, obtainedMap);
    }

    @Test
    @DisplayName("Attempt to retrieve a non-existing map, expect MapNotFoundException")
    void testGetNonExistingMap() {
        // Arrange
        Long mapId = 1L;

        //Expected mapRepository behavior
        when(mapRepository.findById(mapId)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(MapNotFoundException.class, () -> mapService.getMap(mapId));
    }

    @Test
    @DisplayName("Successfully retrieve all maps")
    void testGetAllMapsSuccessfully() {
        // Arrange
        List<Map> expectedMaps = Arrays.asList(new Map(), new Map(), new Map());

        // Expected mapService behavior
        when(mapService.getAllMaps()).thenReturn(expectedMaps);

        // Act
        List<Map> obtainedMaps = mapService.getAllMaps();

        // Assert
        verify(mapRepository, times(1)).findAll();
        assertNotNull(obtainedMaps);
        assertEquals(expectedMaps.size(), obtainedMaps.size());
        assertTrue(expectedMaps.containsAll(obtainedMaps));
    }
}
