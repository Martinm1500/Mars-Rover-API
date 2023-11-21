package com.martinm1500.marsrover.dtos;

import com.martinm1500.marsrover.models.Map;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MapFullDTO {
    private Long id;
    private String name;
    private int dimensionX;
    private int dimensionY;
    private List<ObstacleDTO> obstacles;
    private RoverDTO rover;

    public static MapFullDTO convertFromEntity(Map map) {
        MapFullDTO mapFullDTO = new MapFullDTO();
        mapFullDTO.setId(map.getId());
        mapFullDTO.setName(map.getName());
        mapFullDTO.setDimensionX(map.getDimensionX());
        mapFullDTO.setDimensionY(map.getDimensionY());

        // Convert obstacles to DTOs
        List<ObstacleDTO> obstacleDTOs = map.getObstacles().stream()
                .map(ObstacleDTO::convertToDTO)
                .collect(Collectors.toList());
        mapFullDTO.setObstacles(obstacleDTOs);

        // Convert Rover to DTO
        if (map.getRover() != null) {
            mapFullDTO.setRover(RoverDTO.convertToDTO(map.getRover()));
        }

        return mapFullDTO;
    }
}

