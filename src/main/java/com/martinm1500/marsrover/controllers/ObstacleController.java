package com.martinm1500.marsrover.controllers;

import com.martinm1500.marsrover.dtos.ObstacleDTO;
import com.martinm1500.marsrover.exceptions.InvalidCoordinatesException;
import com.martinm1500.marsrover.exceptions.MapNotFoundException;
import com.martinm1500.marsrover.exceptions.ObstacleNotFoundException;
import com.martinm1500.marsrover.models.Obstacle;
import com.martinm1500.marsrover.services.ObstacleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/obstacles")
public class ObstacleController {

    private final ObstacleService obstacleService;

    @Autowired
    public ObstacleController(ObstacleService obstacleService) {
        this.obstacleService = obstacleService;
    }

    @PostMapping("/create-on-map/{mapId}")
    public ResponseEntity<?> createObstacle(@Valid @RequestBody Obstacle obstacle, @PathVariable Long mapId) {
        try {
            Obstacle createdObstacle = obstacleService.createObstacle(obstacle, mapId);
            ObstacleDTO obstacleDTO = ObstacleDTO.convertToDTO(createdObstacle);
            return ResponseEntity.status(HttpStatus.CREATED).body(obstacleDTO);
        } catch (MapNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (InvalidCoordinatesException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{obstacleId}")
    public ResponseEntity<?> deleteObstacle(@PathVariable Long obstacleId) {
        try {
            obstacleService.deleteObstacle(obstacleId);
            return ResponseEntity.ok("Obstacle with ID: " + obstacleId + " was deleted successfully");
        } catch (ObstacleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get/all-from-map/{mapId}")
    public ResponseEntity<?> getAllObstaclesFromMap(@PathVariable Long mapId) {
        try {
            List<Obstacle> obstacles = obstacleService.getAllFromMap(mapId);
            List<ObstacleDTO> obstacleDTOs = obstacles.stream()
                    .map(ObstacleDTO::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(obstacleDTOs);
        } catch (MapNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

