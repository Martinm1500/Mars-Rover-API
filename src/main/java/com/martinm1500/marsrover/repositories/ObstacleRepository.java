package com.martinm1500.marsrover.repositories;

import com.martinm1500.marsrover.models.Obstacle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObstacleRepository extends JpaRepository<Obstacle,Long> {
    List<Obstacle> findByMapId(Long mapId);
}
