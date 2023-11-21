package com.martinm1500.marsrover.services;


import com.martinm1500.marsrover.exceptions.InvalidCoordinatesException;
import com.martinm1500.marsrover.exceptions.MapNotFoundException;
import com.martinm1500.marsrover.exceptions.ObstacleNotFoundException;
import com.martinm1500.marsrover.models.Map;
import com.martinm1500.marsrover.models.Obstacle;
import com.martinm1500.marsrover.repositories.MapRepository;
import com.martinm1500.marsrover.repositories.ObstacleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ObstacleServiceImpl implements ObstacleService{

    private final MapRepository mapRepository;
    private final ObstacleRepository obstacleRepository;

    @Autowired
    public ObstacleServiceImpl(MapRepository mapRepository, ObstacleRepository obstacleRepository) {
        this.mapRepository = mapRepository;
        this.obstacleRepository = obstacleRepository;
    }

    @Transactional
    @Override
    public Obstacle createObstacle(Obstacle obstacle, Long mapId) {
        Optional<Map> optionalMap = mapRepository.findById(mapId);
        if(optionalMap.isPresent()){
            Map map = optionalMap.get();
            if(map.validPositionOfObstacle(obstacle)){
                map.addObstacle(obstacle);
                obstacle.setMap(map);
                mapRepository.save(map);
                return obstacleRepository.save(obstacle);
            }else{
                throw new InvalidCoordinatesException("The obstacle does not have a valid position on the map. Coordinates: (" + obstacle.getX() + ", " + obstacle.getY() + ")");
            }
        }else {
            throw new MapNotFoundException("Could not find map with ID: " + mapId);
        }
    }

    @Transactional
    @Override
    public void deleteObstacle(Long obstacleId) {
        Optional<Obstacle> obstacleOptional = obstacleRepository.findById(obstacleId);
        if (obstacleOptional.isPresent()) {
            Obstacle obstacle = obstacleOptional.get();
            Map map = obstacle.getMap();

            if (map != null) {
                map.getObstacles().remove(obstacle);
                mapRepository.save(map);
            }
            obstacle.setMap(null);
            obstacleRepository.delete(obstacle);
        } else {
            throw new ObstacleNotFoundException("Could not find obstacle with ID: " + obstacleId);
        }
    }

    @Override
    public Obstacle getObstacle(Long obstacleId)  {
        Optional<Obstacle> optionalObstacle = obstacleRepository.findById(obstacleId);
        if(optionalObstacle.isPresent()){
            return optionalObstacle.get();
        }else{
            throw new ObstacleNotFoundException("Could not find obstacle with ID: " + obstacleId);
        }
    }

    @Override
    public List<Obstacle> getAllFromMap(Long mapId)  {
        if(mapRepository.existsById(mapId)){
            return obstacleRepository.findByMapId(mapId);
        }else{
            throw new MapNotFoundException("Could not find map with ID: " + mapId);
        }
    }
}
