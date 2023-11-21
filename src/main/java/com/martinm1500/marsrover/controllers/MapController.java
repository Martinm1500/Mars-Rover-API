package com.martinm1500.marsrover.controllers;

import com.martinm1500.marsrover.dtos.MapDTO;
import com.martinm1500.marsrover.dtos.MapFullDTO;
import com.martinm1500.marsrover.exceptions.InvalidMapDimensionsException;
import com.martinm1500.marsrover.exceptions.MapNotFoundException;
import com.martinm1500.marsrover.models.Map;
import com.martinm1500.marsrover.services.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/maps")
public class MapController {

    private final MapService mapService;
    @Autowired
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }
    @PostMapping("/create")
    public ResponseEntity<?> createMap(@Valid @RequestBody Map map) {
        try {
            Map createdMap = mapService.createMap(map);
            MapDTO mapDTO = MapDTO.convertToDTO(createdMap);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapDTO);
        } catch (InvalidMapDimensionsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMap(@PathVariable Long mapId) {
        try {
            mapService.deleteMap(mapId);
            return ResponseEntity.ok("Map with ID: " + mapId + " was deleted successfully");
        } catch (MapNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMap(@PathVariable Long id) {
        try {
            Map obtainedMap = mapService.getMap(id);
            MapFullDTO mapFullDTO = MapFullDTO.convertFromEntity(obtainedMap);
            return ResponseEntity.ok(mapFullDTO);
        } catch (MapNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/get/all")
    public ResponseEntity<List<MapDTO>> getAllMaps() {
        List<Map> maps = mapService.getAllMaps();
        List<MapDTO> mapDTOs = maps.stream()
                .map(MapDTO::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(mapDTOs);
    }
}

