package com.martinm1500.marsrover.dtos;

import com.martinm1500.marsrover.models.Obstacle;
import lombok.Data;

@Data
public class ObstacleDTO {
    private int x;
    private int y;

    public static ObstacleDTO convertToDTO(Obstacle obstacle) {
        ObstacleDTO obstacleDTO = new ObstacleDTO();
        obstacleDTO.setX(obstacle.getX());
        obstacleDTO.setY(obstacle.getY());
        return obstacleDTO;
    }
}
