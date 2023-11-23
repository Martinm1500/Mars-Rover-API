package com.martinm1500.marsrover.services;

import com.martinm1500.marsrover.exceptions.*;
import com.martinm1500.marsrover.models.Map;
import com.martinm1500.marsrover.models.Obstacle;
import com.martinm1500.marsrover.models.Rover;
import com.martinm1500.marsrover.repositories.MapRepository;
import com.martinm1500.marsrover.repositories.RoverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoverServiceImpl implements RoverService{

    private final RoverRepository roverRepository;
    private final MapRepository mapRepository;

    @Autowired
    public RoverServiceImpl(RoverRepository roverRepository, MapRepository mapRepository) {
        this.roverRepository = roverRepository;
        this.mapRepository = mapRepository;
    }

    @Transactional
    @Override
    public Rover createRover(Rover rover, Long mapId){
        Optional<Map> optionalMap = mapRepository.findById(mapId);
        if(optionalMap.isPresent()){
            Map map = optionalMap.get();
            int x = rover.getX();
            int y = rover.getY();

            if(map.getRover() != null){
                throw new InvalidOperationException("The map already has a rover" + map.getRover());
            }
            if(map.isPositionOccupied(x,y)){
                throw new InvalidCoordinatesException("position ( "+ x +" , " + y +" ) is occupied by an obstacle");
            }
            if(!Rover.isValidPosition(x,y,map)){
                throw new InvalidCoordinatesException("The rover's coordinates do not represent a valid position on the map");
            }
            if(!Rover.isValidOrientation(rover.getOrientation())){
                throw new InvalidOrientationException("Invalid rover orientation. Accepted values are: " +
                        Rover.NORTH + ", " + Rover.SOUTH + ", " + Rover.EAST + ", or " + Rover.WEST);
            }
            map.setRover(rover);
            rover.setMap(map);
            mapRepository.save(map);
            return roverRepository.save(rover);
        }else{
            throw new MapNotFoundException("Could not find map with ID: " + mapId);
        }
    }

    @Transactional
    @Override
    public void deleteRover(Long roverId){
        Optional<Rover> optionalRover = roverRepository.findById(roverId);
        if(optionalRover.isPresent()){
            Map map = optionalRover.get().getMap();
            map.setRover(null);
            roverRepository.deleteById(roverId);
        }else{
            throw new RoverNotFoundException("Could not find rover with ID: " + roverId);
        }
    }

    @Override
    public Rover updateRover(Rover rover){
        Long roverId = rover.getId();
        Optional<Rover> optionalRover = roverRepository.findById(roverId);
        if(optionalRover.isPresent()){
            Map map = optionalRover.get().getMap();

            int x = rover.getX();
            int y = rover.getY();
            char orientation = rover.getOrientation();

            if(!Rover.isValidPosition(x,y,map)){
                throw new InvalidCoordinatesException("The rover's coordinates do not represent a valid position on the map");
            }
            if(map.isPositionOccupied(x,y)){
                throw new InvalidCoordinatesException("position ( "+ x +" , " + y +" ) is occupied by an obstacle");
            }
            if(!Rover.isValidOrientation(orientation)){
                throw new InvalidOrientationException("Invalid rover orientation. Accepted values are: " +
                        Rover.NORTH + ", " + Rover.SOUTH + ", " + Rover.EAST + ", or " + Rover.WEST);
            }
            map.setRover(rover);
            rover.setMap(map);
            return roverRepository.save(rover);

        }else{
            throw new RoverNotFoundException("Could not find rover with ID: " + roverId);
        }
    }


    @Override
    public Rover getRover(Long roverId){
        Optional<Rover> optionalRover = roverRepository.findById(roverId);
        if(optionalRover.isPresent()){
            return optionalRover.get();
        }else {
            throw new RoverNotFoundException("Could not find rover with ID: " + roverId);
        }
    }

    @Override
    public Rover getRoverByMapId(Long mapId){
        Optional<Map> optionalMap = mapRepository.findById(mapId);
        if(optionalMap.isPresent()){
            return optionalMap.get().getRover();
        }else{
            throw new MapNotFoundException("No rover found for map with ID: " + mapId);
        }
    }

    @Transactional
    @Override
    public Obstacle executeCommands(Long roverId, List<Character> commands){
        Optional<Rover> optionalRover = roverRepository.findById(roverId);
        if (optionalRover.isPresent()){
            Rover rover = optionalRover.get();
            Obstacle reportedObstacle = rover.executeCommands(commands);
            roverRepository.save(rover);
            return reportedObstacle;
        }else{
            throw new RoverNotFoundException("Could not find rover with ID: " + roverId);
        }
    }
}
