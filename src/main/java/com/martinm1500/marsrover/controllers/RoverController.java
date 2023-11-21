package com.martinm1500.marsrover.controllers;

import com.martinm1500.marsrover.dtos.RoverDTO;
import com.martinm1500.marsrover.exceptions.*;
import com.martinm1500.marsrover.models.Obstacle;
import com.martinm1500.marsrover.models.Rover;
import com.martinm1500.marsrover.services.RoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/rovers")
public class RoverController {

    private final RoverService roverService;

    @Autowired
    public RoverController(RoverService roverService) {
        this.roverService = roverService;
    }

    @PostMapping("/create-on-map/{mapId}")
    public ResponseEntity<?> createRover(@PathVariable Long mapId,@Valid @RequestBody Rover rover){
        try{
            Rover createdRover = roverService.createRover(rover,mapId);
            RoverDTO roverDTO = RoverDTO.convertToDTO(createdRover);
            roverDTO.setMapId(mapId);

            return ResponseEntity.status(HttpStatus.CREATED).body(roverDTO);
        }catch (MapNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (InvalidCoordinatesException | InvalidOrientationException | InvalidOperationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{roverId}")
    public ResponseEntity<?> deleteRover(@PathVariable Long roverId) {
        try {
            roverService.deleteRover(roverId);
            return ResponseEntity.ok("Rover with ID: " + roverId + " was deleted successfully");
        } catch (RoverNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateRover(@Valid @RequestBody Rover rover) {
        try {
            Rover updatedRover = roverService.updateRover(rover);
            RoverDTO roverDTO = RoverDTO.convertToDTO(updatedRover);
            return ResponseEntity.ok(roverDTO);
        } catch (RoverNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidCoordinatesException | InvalidOrientationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{roverId}")
    public ResponseEntity<?> getRover(@PathVariable Long roverId) {
        try {
            Rover obtainedRover = roverService.getRover(roverId);
            RoverDTO roverDTO = RoverDTO.convertToDTO(obtainedRover);
            return ResponseEntity.ok(roverDTO);
        } catch (RoverNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/by-map/{mapId}")
    public ResponseEntity<?> getRoverByMapId(@PathVariable Long mapId) {
        try {
            Rover obtainedRover = roverService.getRoverByMapId(mapId);
            RoverDTO roverDTO = RoverDTO.convertToDTO(obtainedRover);
            return ResponseEntity.ok(roverDTO);
        } catch (MapNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/execute-commands/{roverId}")
    public ResponseEntity<?> executeCommands(@PathVariable Long roverId, @RequestBody List<Character> commands) {
        try {
            Obstacle obstacle = roverService.executeCommands(roverId, commands);
            if(obstacle!= null){
                // Mission aborted due to an obstacle. Reporting and returning the obstacle.
                String message = "Mission aborted due to obstacle at coordinates: " +
                        "(" + obstacle.getX() + ", " + obstacle.getY() + ")";
                return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
            }
            return ResponseEntity.ok().build();
        } catch (RoverNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidCommandException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

