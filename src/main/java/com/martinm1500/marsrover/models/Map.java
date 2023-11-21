package com.martinm1500.marsrover.models;

import com.martinm1500.marsrover.exceptions.InvalidCoordinatesException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Map {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    @NotNull
    private int dimensionX;
    @NotNull
    private int dimensionY;

    @OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Obstacle> obstacles = new ArrayList<>();

    @OneToOne(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
    private Rover rover;

    public Map(int dimensionX, int dimensionY, String name) {
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        this.name = name;
    }

    public Map(){}

    public void removeObstacle(Obstacle obstacle) {
        obstacles.remove(obstacle);
        obstacle.setMap(null);
    }
    public void addObstacle(Obstacle obstacle){
        if(validPositionOfObstacle(obstacle)){
            obstacle.setMap(this);
            obstacles.add(obstacle);
        }else {
            throw new InvalidCoordinatesException("The obstacle's coordinates do not represent a valid position on the map");
        }
    }

    public static boolean isValidDimensions(int dimensionX, int dimensionY) {
        return dimensionX > 0 && dimensionY > 0 && dimensionX % 2 == 0 && dimensionY % 2 == 0;
    }
    public boolean isPositionOccupied(int x, int y){
        boolean occupied = false;
        for (Obstacle obstacle : obstacles){
            occupied = occupied || obstacle.isAt(x,y);
        }
        return occupied;
    }

    public boolean validPositionOfObstacle(Obstacle obstacle){
        int x = obstacle.getX();
        int y = obstacle.getY();
        return x>=1 && x <= dimensionX && y >=1 && y <=dimensionY;
    }

    public Obstacle getObstacle(int x, int y){
        for(Obstacle obstacle: obstacles){
            if(obstacle.isAt(x,y)){
                return obstacle;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Map map)) return false;
        return dimensionX == map.dimensionX && dimensionY == map.dimensionY && Objects.equals(id, map.id) && Objects.equals(name, map.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dimensionX, dimensionY, name);
    }

    @Override
    public String toString() {
        return "Map{" +
                "id=" + id +
                ", dimensionX=" + dimensionX +
                ", dimensionY=" + dimensionY +
                ", name='" + name + '\'' +
                '}';
    }
}
