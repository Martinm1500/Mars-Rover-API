package com.martinm1500.marsrover.services;

import com.martinm1500.marsrover.exceptions.InvalidMapDimensionsException;
import com.martinm1500.marsrover.exceptions.MapNotFoundException;
import com.martinm1500.marsrover.models.Map;
import com.martinm1500.marsrover.repositories.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MapServiceImpl implements MapService{

    private final MapRepository mapRepository;

    @Autowired
    public MapServiceImpl(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    @Transactional
    @Override
    public Map createMap(Map map){
        int dimensionX = map.getDimensionX();
        int dimensionY = map.getDimensionY();
        if(Map.isValidDimensions(dimensionX, dimensionY)){
            return mapRepository.save(map);
        }else {
            throw new InvalidMapDimensionsException("Only positive even dimensions are allowed.");
        }
    }

    @Transactional
    @Override
    public void deleteMap(Long id){
        if(mapRepository.existsById(id)){
            mapRepository.deleteById(id);
        }else{
            throw new MapNotFoundException("Could not find map with ID: " + id);
        }
    }

    @Override
    public Map getMap(Long id){
        Optional<Map> optionalMap = mapRepository.findById(id);
        if(optionalMap.isPresent()){
            return optionalMap.get();
        }else{
            throw new MapNotFoundException("Could not find map with ID: " + id);
        }
    }

    @Override
    public List<Map> getAllMaps() {
        return mapRepository.findAll();
    }
}
