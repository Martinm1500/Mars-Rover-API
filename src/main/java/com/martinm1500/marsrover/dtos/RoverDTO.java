package com.martinm1500.marsrover.dtos;

import com.martinm1500.marsrover.models.Rover;
import lombok.Data;

@Data
public class RoverDTO {
    private Long id;
    private int x;
    private int y;
    private char orientation;
    private Long mapId;
    public static RoverDTO convertToDTO(Rover rover) {
        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setId(rover.getId());
        roverDTO.setX(rover.getX());
        roverDTO.setY(rover.getY());
        roverDTO.setOrientation(rover.getOrientation());
        return roverDTO;
    }
}
