package com.martinm1500.marsrover.services;

import com.martinm1500.marsrover.exceptions.*;
import com.martinm1500.marsrover.models.Map;
import com.martinm1500.marsrover.models.Obstacle;
import com.martinm1500.marsrover.models.Rover;
import com.martinm1500.marsrover.repositories.MapRepository;
import com.martinm1500.marsrover.repositories.RoverRepository;
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
public class RoverServiceTest {

    @Mock
    private MapRepository mapRepository;

    @Mock
    private RoverRepository roverRepository;

    @InjectMocks
    private RoverServiceImpl roverService;

    @Test
    @DisplayName("Given a rover and the ID of a map, successfully create rover on the map")
    void testCreateRoverSuccessfully(){
        //Arrange
        Long mapId = 1L;
        Map map = new Map(12,12,"Jupiter");
        map.setId(1L);
        Rover rover = new Rover(4,4,Rover.NORTH);
        rover.setId(1L);

        //Expected repositories behavior
        when(mapRepository.findById(mapId)).thenReturn(Optional.of(map));
        when(roverRepository.save(rover)).thenReturn(rover);

        //Act
        Rover createdRover = roverService.createRover(rover,mapId);

        //Assert
        verify(mapRepository,times(1)).save(map);
        verify(roverRepository,times(1)).save(rover);
        assertEquals(rover,createdRover);
        assertEquals(map,rover.getMap());
        assertEquals(rover,map.getRover());
    }

    @Test
    @DisplayName("Attempt to create a rover on a non-existing map. MapNotFoundException is thrown.")
    void testCreateRoverOnNonExistingMapThrowsException(){
        //Arrange
        Long mapId = 1L;
        Rover rover = new Rover(4,4,Rover.NORTH);

        //Expected repositories behavior
        when(mapRepository.findById(mapId)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(MapNotFoundException.class, () -> roverService.createRover(rover,mapId));

        //Assert
        verify(mapRepository,never()).save(any());
        verify(roverRepository,never()).save(rover);
    }

    @Test
    @DisplayName("Attempt to create a rover at coordinates that do not represent a position on the map. InvalidCoordinatesException is thrown.")
    void testCreateRoverAtInvalidCoordinatesThrowsException(){
        //Arrange
        Long mapId = 1L;
        Map map = new Map(12,12,"Jupiter");
        Rover rover = new Rover(4,15,Rover.NORTH);

        //Expected repositories behavior
        when(mapRepository.findById(mapId)).thenReturn(Optional.of(map));

        //Act and Assert
        assertThrows(InvalidCoordinatesException.class, () -> roverService.createRover(rover,mapId));

        //Assert
        verify(mapRepository,never()).save(any());
        verify(roverRepository,never()).save(rover);
    }

    @Test
    @DisplayName("Attempt to create a rover with invalid orientation. InvalidOrientationException is thrown.")
    void testCreateRoverWithInvalidOrientationThrowsException(){
        //Arrange
        Long mapId = 1L;
        Map map = new Map(12,12,"Jupiter");
        Rover rover = new Rover(4,4,'n');

        //Expected repositories behavior
        when(mapRepository.findById(mapId)).thenReturn(Optional.of(map));

        //Act and Assert
        assertThrows(InvalidOrientationException.class, () -> roverService.createRover(rover,mapId));

        //Assert
        verify(mapRepository,never()).save(any());
        verify(roverRepository,never()).save(rover);
    }


    @Test
    @DisplayName("Attempt to create a rover at coordinates occupied by an obstacle.")
    void testCreateRoverAtOccupiedCoordinatesThrowsException(){
        //Arrange
        Long mapId = 1L;
        Map map = new Map(12,12,"Jupiter");
        map.addObstacle(new Obstacle(4,4));
        Rover rover = new Rover(4,4,Rover.NORTH);

        //Expected repositories behavior
        when(mapRepository.findById(mapId)).thenReturn(Optional.of(map));

        //Act and Assert
        assertThrows(InvalidCoordinatesException.class, () -> roverService.createRover(rover,mapId));

        //Assert
        verify(mapRepository,never()).save(any());
        verify(roverRepository,never()).save(rover);
    }

    @Test
    @DisplayName("Attempt to create a rover on a map that already has a rover.")
    void testCreateRoverOnMapWithExistingRoverThrowsException(){
        //Arrange
        Long mapId = 1L;
        Map map = new Map(12,12,"Jupiter");
        map.setRover(new Rover());
        Rover rover = new Rover(4,4,Rover.NORTH);

        //Expected repositories behavior
        when(mapRepository.findById(mapId)).thenReturn(Optional.of(map));

        //Act and Assert
        assertThrows(InvalidOperationException.class, () -> roverService.createRover(rover,mapId));

        //Assert
        verify(mapRepository,never()).save(any());
        verify(roverRepository,never()).save(rover);
    }

    @Test
    @DisplayName("Given the ID of a rover, successfully delete it.")
    void testDeleteRoverSuccessfully(){
        // Arrange
        Long roverId = 1L;
        Rover rover = new Rover(4,4,Rover.NORTH);
        Map map = new Map(12,12,"Jupiter");
        map.setRover(rover);
        rover.setMap(map);

        // Expected repository behavior
        when(roverRepository.findById(roverId)).thenReturn(Optional.of(rover));

        // Act
        roverService.deleteRover(roverId);

        // Assert
        verify(roverRepository, times(1)).deleteById(roverId);
        assertNull(map.getRover());
    }

    @Test
    @DisplayName("Attempt to delete a rover that does not exist. RoverNotFoundException is thrown.")
    void testDeleteNonExistingRoverThrowsException(){
        //Arrange
        Long roverId = 1L;

        //Expected repository behavior
        when(roverRepository.findById(roverId)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(RoverNotFoundException.class, () -> roverService.deleteRover(roverId));
    }

    @Test
    @DisplayName("Successfully update a rover.")
    void testUpdateRoverSuccessfully(){
        //Arrange
        Long roverId = 1L;
        Long mapId = 7L;
        Map map = new Map(12,12,"Jupiter");
        map.setId(mapId);
        Rover currentrover = new Rover(4,4,Rover.NORTH);
        currentrover.setId(roverId);

        map.setRover(currentrover);
        currentrover.setMap(map);

        Rover roverUpdate = new Rover(1,3,Rover.SOUTH);
        roverUpdate.setId(roverId);

        //Expected repositories behavior
        when(roverRepository.findById(roverId)).thenReturn(Optional.of(currentrover));
        when(roverRepository.save(roverUpdate)).thenReturn(roverUpdate);

        //Act
        Rover updatedRover = roverService.updateRover(roverUpdate);

        //Assert
        verify(roverRepository, times(1)).save(eq(roverUpdate));
        assertEquals(roverUpdate, updatedRover);
        assertEquals(map.getRover(), updatedRover);
        assertEquals(updatedRover.getMap(), map);
    }

    @Test
    @DisplayName("Attempt to update a rover that does not exist. RoverNotFoundException is thrown.")
    void testUpdateNonExistingRoverThrowsException(){
        // Arrange
        Long roverId = 1L;
        Rover roverUpdate = new Rover(4, 4, Rover.SOUTH);
        roverUpdate.setId(roverId);

        // Expected repository behavior
        // Expects that when attempting to find a rover with the provided ID, the repository returns Optional.empty().
        when(roverRepository.findById(roverId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RoverNotFoundException.class, () -> roverService.updateRover(roverUpdate));

        // Verify that no interactions occurred with mapRepository and roverRepository
        verify(mapRepository, never()).save(any());
        verify(roverRepository, never()).save(any());

        // Verify that no more interactions occurred with any repositories
        verifyNoMoreInteractions(mapRepository, roverRepository);
    }

    @Test
    @DisplayName("Attempt to update a rover to coordinates that do not represent a position on the map. InvalidCoordinatesException is thrown.")
    void testUpdateRoverToInvalidCoordinatesThrowsException(){
        //Arrange
        Long roverId = 1L;
        Map map = new Map(12,12,"Jupiter");
        Rover currentrover = new Rover(4,4,Rover.NORTH);
        currentrover.setId(roverId);

        map.setRover(currentrover);
        currentrover.setMap(map);

        Rover roverUpdate = new Rover(23,15,Rover.SOUTH);
        roverUpdate.setId(roverId);


        //Expected repositories behavior
        when(roverRepository.findById(roverId)).thenReturn(Optional.of(currentrover));

        //Act and Assert
        assertThrows(InvalidCoordinatesException.class, ()-> roverService.updateRover(roverUpdate));

        // Verify that no interactions occurred with mapRepository and roverRepository
        verify(mapRepository, never()).save(any());
        verify(roverRepository, never()).save(any());

        // Verify that no more interactions occurred with any repositories
        verifyNoMoreInteractions(mapRepository, roverRepository);

    }

    @Test
    @DisplayName("Attempt to update a rover to coordinates occupied by an obstacle.")
    void testUpdateRoverToOccupiedCoordinatesThrowsException(){
        //Arrange
        Long roverId = 1L;
        Map map = new Map(12,12,"Jupiter");
        Rover currentrover = new Rover(4,4,Rover.NORTH);
        currentrover.setId(roverId);

        map.setRover(currentrover);
        currentrover.setMap(map);
        map.addObstacle(new Obstacle(3,3));

        Rover roverUpdate = new Rover(3,3,Rover.SOUTH);
        roverUpdate.setId(roverId);


        //Expected repositories behavior
        when(roverRepository.findById(roverId)).thenReturn(Optional.of(currentrover));

        //Act and Assert
        assertThrows(InvalidCoordinatesException.class, ()-> roverService.updateRover(roverUpdate));

        // Verify that no interactions occurred with mapRepository and roverRepository
        verify(mapRepository, never()).save(any());
        verify(roverRepository, never()).save(any());

        // Verify that no more interactions occurred with any repositories
        verifyNoMoreInteractions(mapRepository, roverRepository);
    }

    @Test
    @DisplayName("Attempt to update a rover with invalid orientation should throw InvalidOrientationException.")
    void testUpdateRoverWithInvalidOrientationThrowsException() {
        // Arrange
        Long roverId = 1L;
        Map map = new Map(12, 12, "Jupiter");
        Rover currentRover = new Rover(4, 4, Rover.NORTH);
        currentRover.setId(roverId);

        map.setRover(currentRover);
        currentRover.setMap(map);

        Rover roverUpdate = new Rover(4, 4, 'X'); // 'X' is an invalid orientation
        roverUpdate.setId(roverId);

        // Expected repository behavior
        // Expects that when attempting to find a rover with the provided ID, the repository returns the current rover.
        when(roverRepository.findById(roverId)).thenReturn(Optional.of(currentRover));

        // Act and Assert
        assertThrows(InvalidOrientationException.class, () -> roverService.updateRover(roverUpdate));

        // Verify that no interactions occurred with mapRepository and roverRepository
        verify(mapRepository, never()).save(any());
        verify(roverRepository, never()).save(any());

        // Verify that no more interactions occurred with any repositories
        verifyNoMoreInteractions(mapRepository, roverRepository);
    }


    @Test
    @DisplayName("Given the ID of a rover, successfully retrieve it.")
    void testGetRoverSuccessfully(){
        //Arrange
        Long roverId = 1L;
        Rover rover = new Rover(4,4,Rover.SOUTH);

        //Expected repository behavior
        when(roverRepository.findById(roverId)).thenReturn(Optional.of(rover));

        //Act
        Rover obtainedRover = roverService.getRover(roverId);

        //Assert
        verify(roverRepository, times(1)).findById(roverId);
        assertNotNull(obtainedRover);
        assertSame(rover, obtainedRover);

        // Additional verification to ensure no more interactions with the repository
        verifyNoMoreInteractions(roverRepository);
    }

    @Test
    @DisplayName("Attempt to retrieve a rover that does not exist. RoverNotFoundException is thrown.")
    void testGetNonExistingRoverThrowsException(){
        //Arrange
        Long roverId = 1L;

        //Expected repository behavior
        when(roverRepository.findById(roverId)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(RoverNotFoundException.class, () -> roverService.getRover(roverId));

        // Additional verification to ensure no more interactions with the repository
        verifyNoMoreInteractions(roverRepository);
    }

    @Test
    @DisplayName("Given the ID of a map, retrieve the rover on that map.")
    void testGetRoverByMapIdSuccessfully(){
        //Arrange
        Long mapId = 1L;
        Map map = new Map(12,12,"Jupiter");
        Rover rover = new Rover(4,4,Rover.SOUTH);
        map.setRover(rover);

        //Expected repository behavior
        when(mapRepository.findById(mapId)).thenReturn(Optional.of(map));

        //Act
        Rover obtainedRover = roverService.getRoverByMapId(mapId);

        //Assert
        assertNotNull(obtainedRover);

        // Additional verification to ensure no more interactions with the repository
        verifyNoMoreInteractions(mapRepository);
    }
    @Test
    @DisplayName("Given the ID of a map without a rover, return null.")
    void testGetRoverByMapIdWithNullRover() {
        // Arrange
        Long mapId = 1L;
        Map map = new Map(12, 12, "Jupiter");

        // Expected repository behavior
        when(mapRepository.findById(mapId)).thenReturn(Optional.of(map));

        // Act
        Rover obtainedRover = roverService.getRoverByMapId(mapId);

        // Assert
        assertNull(obtainedRover);

        // Additional verification to ensure no more interactions with the repositories
        verifyNoMoreInteractions(roverRepository, mapRepository);
    }


    @Test
    @DisplayName("Attempt to retrieve a rover from a non-existing map. MapNotFoundException is thrown.")
    void testGetRoverFromNonExistingMapThrowsException(){
        // Arrange
        Long mapId = 100L;

        // Expected repository behavior
        when(mapRepository.findById(mapId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(MapNotFoundException.class, () -> roverService.getRoverByMapId(mapId));

        // Additional verification to ensure no more interactions with the repositories
        verifyNoMoreInteractions(roverRepository, mapRepository);
    }

    @Test
    @DisplayName("Execute commands successfully. Returns null, does not report an obstacle.")
    void testExecuteCommandsSuccessfullyNoObstacleReported(){
        //Arrange
        Long roverId = 1L;
        Rover rover = new Rover(1,1,Rover.EAST);
        Map map = new Map(8,8,"Tierra");
        rover.setMap(map);

        List<Character> commands = Arrays.asList('f','f','f','f');

        //Expected behavior repository
        when(roverRepository.findById(roverId)).thenReturn(Optional.of(rover));

        //Act
        Obstacle obstacle = roverService.executeCommands(roverId,commands);

        //Assert
        verify(roverRepository, times(1)).save(rover);
        assertNull(obstacle);
    }

    @Test
    @DisplayName("Execute commands successfully. Returns an obstacle, reports the found obstacle.")
    void testExecuteCommandsSuccessfullyWithObstacleReported(){

        //Arrange
        Long roverId = 1L;
        Rover rover = new Rover(1,3,Rover.NORTH);
        Map map = new Map(8,8,"Tierra");
        Obstacle obstacle = new Obstacle(1,1);
        map.addObstacle(obstacle);
        rover.setMap(map);

        List<Character> commands = Arrays.asList('f','f');

        //Expected repository behavior
        when(roverRepository.findById(roverId)).thenReturn(Optional.of(rover));

        //act
        Obstacle reportedObstacle = roverService.executeCommands(roverId,commands);  //retorna un obstaculo

        //Assert
        verify(roverRepository, times(1)).save(rover);
        assertNotNull(reportedObstacle);
    }

    @Test
    @DisplayName("Attempt to execute commands for a non-existing rover. RoverNotFoundException is thrown.")
    void testExecuteCommandsForNonExistingRoverThrowsException(){
        //Arrange
        Long roverId = 1L;
        List<Character> commands = Arrays.asList('f','f','f','f');

        //Expected repository behavior
        when(roverRepository.findById(roverId)).thenThrow(RoverNotFoundException.class);

        //act and Assert
        assertThrows(RoverNotFoundException.class, () -> roverService.executeCommands(roverId,commands));
        verify(roverRepository, never()).save(any());
    }

    @Test
    @DisplayName("Attempt to execute invalid commands. InvalidCommandException is thrown.")
    void testExecuteInvalidCommandsThrowsException(){
        //Arrange
        Long roverId = 1L;
        Rover rover = new Rover(1,1,Rover.EAST);
        List<Character> commands = Arrays.asList('g','f','f','f');

        //Expected repository behavior
        when(roverRepository.findById(roverId)).thenReturn(Optional.of(rover));

        //act and Assert
        assertThrows(InvalidCommandException.class, () -> roverService.executeCommands(roverId,commands));
        verify(roverRepository, never()).save(any());
    }
}
