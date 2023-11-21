package com.martinm1500.marsrover.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.martinm1500.marsrover.exceptions.InvalidCommandException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Rover extends RoverControl implements Geolocation{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "map_id")
    private Map map;

    @Transient
    private Obstacle reportedObstacle;

    @NotNull
    private int x;
    @NotNull
    private int y;
    @NotNull
    private char orientation;

    public static final char NORTH = 'N';
    public static final char SOUTH = 'S';
    public static final char EAST = 'E';
    public static final char WEST = 'W';

    public static final char MOVE_FORDWARD = 'f';
    public static final char MOVE_BACKWARD = 'b';
    public static final char TURN_LEFT = 'l';
    public static final char TURN_RIGHT = 'r';

    public Rover(int x, int y, char orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    public Rover(){}

    @Override
    public Obstacle executeCommands(List<Character> commands){
        for(Character command : commands){
            if(!isValidCommand(command)){
                throw new InvalidCommandException("Invalid command: " + command);
            }
            switch (command){
                case MOVE_FORDWARD -> moveForward();
                case MOVE_BACKWARD -> moveBackward();
                case TURN_RIGHT -> turnRight();
                case TURN_LEFT -> turnLeft();
            }
        }
        return reportedObstacle;
    }

    @Override
    protected void moveForward() {
        int mapDimensionX = map.getDimensionX();
        switch (orientation) {
            case NORTH -> {
                if (onNorthPole()) {
                    if (onMeridianLeftSide()) {
                        if(reportObstacle(x + mapDimensionX/2,y)){
                            break;
                        }
                        x += mapDimensionX / 2;
                        orientation = SOUTH;
                    } else if (onMeridianRightSide()) {
                        if(reportObstacle(x - mapDimensionX/2,y)){
                            break;
                        }
                        x -= mapDimensionX / 2;
                        orientation = SOUTH;
                    }
                } else {
                    if(reportObstacle(x,y-1)){
                        break;
                    }
                    y--;
                }
            }
            case SOUTH -> {
                if (onSouthPole()) {
                    if (onMeridianLeftSide()) {
                        if(reportObstacle(x + mapDimensionX/2,y)){
                            break;
                        }
                        x += mapDimensionX / 2;
                        orientation = NORTH;
                    } else if (onMeridianRightSide()) {
                        if(reportObstacle(x - mapDimensionX/2,y)){
                            break;
                        }
                        x -= mapDimensionX / 2;
                        orientation = NORTH;
                    }
                } else {
                    if(reportObstacle(x,y+1)){
                        break;
                    }
                    y++;
                }
            }
            case EAST -> {
                if (x == map.getDimensionX()) {
                    if(reportObstacle(1,y)){
                        break;
                    }
                    x = 1;
                } else {
                    if(reportObstacle(x+1,y)){
                        break;
                    }
                    x++;
                }
            }
            case WEST -> {
                if (x == 1) {
                    if(reportObstacle(mapDimensionX,y)){
                        break;
                    }
                    x = mapDimensionX;
                } else {
                    if(reportObstacle(x - 1,y)){
                        break;
                    }
                    x--;
                }
            }
        }
    }

    @Override
    protected void moveBackward() {
        int mapDimensionX = map.getDimensionX();
        switch (orientation) {
            case NORTH -> {
                if (onSouthPole()) {
                    if (onMeridianLeftSide()) {
                        if(reportObstacle(x + mapDimensionX/2,y)){
                            break;
                        }
                        x += mapDimensionX / 2;
                        orientation = SOUTH;
                    } else if (onMeridianRightSide()) {
                        if(reportObstacle(x - mapDimensionX/2,y)){
                            break;
                        }
                        x -= mapDimensionX / 2;
                        orientation = SOUTH;
                    }
                } else {
                    if(reportObstacle(x,y+1)){
                        break;
                    }
                    y++;
                }
            }
            case SOUTH -> {
                if (onNorthPole()) {
                    if (onMeridianLeftSide()) {
                        if(reportObstacle(x + mapDimensionX/2,y)){
                            break;
                        }
                        x += mapDimensionX / 2;
                        orientation = NORTH;
                    } else if (onMeridianRightSide()) {
                        if(reportObstacle(x - mapDimensionX/2,y)){
                            break;
                        }
                        x -= mapDimensionX / 2;
                        orientation = NORTH;
                    }
                } else {
                    if(reportObstacle(x,y - 1)){
                        break;
                    }
                    y--;
                }
            }
            case EAST -> {
                if (x == 1) {
                    if(reportObstacle(mapDimensionX,y)){
                        break;
                    }
                    x = mapDimensionX;
                } else {
                    if(reportObstacle(x - 1,y)){
                        break;
                    }
                    x--;
                }
            }
            case WEST -> {
                if (x == mapDimensionX) {
                    if(reportObstacle(1,y)){
                        break;
                    }
                    x = 1;
                } else {
                    if(reportObstacle(x+1,y)){
                        break;
                    }
                    x++;
                }
            }
        }
    }

    @Override
    protected void turnRight() {
        switch (orientation){
            case NORTH -> orientation = EAST;
            case SOUTH -> orientation = WEST;
            case EAST -> orientation = SOUTH;
            case WEST -> orientation = NORTH;
        }
    }

    @Override
    protected void turnLeft() {
        switch (orientation){
            case NORTH -> orientation = WEST;
            case SOUTH -> orientation = EAST;
            case EAST -> orientation = NORTH;
            case WEST -> orientation = SOUTH;
        }
    }


    @Override
    public boolean onMeridianLeftSide(){
        return x >= 1 && x <= map.getDimensionY()/2;
    }

    @Override
    public boolean onMeridianRightSide(){
        return x> map.getDimensionY()/2 && x<= map.getDimensionY();
    }

    @Override
    public boolean onNorthPole(){
        return y==1;
    }

    @Override
    public boolean onSouthPole(){
        return y == map.getDimensionY();
    }


    public static boolean isValidCommand(char command) {
        return command == MOVE_FORDWARD || command == MOVE_BACKWARD || command == TURN_RIGHT || command == TURN_LEFT;
    }


    public static boolean isValidOrientation(char orientation){
        return orientation == NORTH || orientation == SOUTH || orientation == EAST || orientation == WEST;
    }

    public static boolean isValidPosition(int x, int y, Map map) {
        return x >= 1 && x <= map.getDimensionX() && y >= 1 && y <= map.getDimensionY();
    }

    private boolean reportObstacle(int x, int y){
        if(map.isPositionOccupied(x,y)){
            reportedObstacle = map.getObstacle(x,y);
            return true;
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rover rover)) return false;
        return x == rover.x && y == rover.y && orientation == rover.orientation && Objects.equals(id, rover.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, orientation);
    }

    @Override
    public String toString() {
        return "Rover{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", orientation=" + orientation +
                '}';
    }
}
