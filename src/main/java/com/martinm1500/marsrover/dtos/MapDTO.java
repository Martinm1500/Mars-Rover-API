package com.martinm1500.marsrover.dtos;

import com.martinm1500.marsrover.models.Map;
import lombok.Data;

@Data
public class MapDTO {
    private Long id;
    private String name;
    private int dimensionX;
    private int dimensionY;

    public static MapDTO convertToDTO(Map map) {
        MapDTO mapDTO = new MapDTO();
        mapDTO.setId(map.getId());
        mapDTO.setName(map.getName());
        mapDTO.setDimensionX(map.getDimensionX());
        mapDTO.setDimensionY(map.getDimensionY());
        return mapDTO;
    }
}
