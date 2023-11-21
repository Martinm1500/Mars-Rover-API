package com.martinm1500.marsrover.models;

import java.util.List;

/**
 * Abstract class defining basic rover control operations.
 */
public abstract class RoverControl {

    /**
     * Allows the rover to move forward.
     */
    protected abstract void moveForward();

    /**
     * Allows the rover to move backward.
     */
    protected abstract void moveBackward();

    /**
     * Allows the rover to turn to the right.
     */
    protected abstract void turnRight();

    /**
     * Allows the rover to turn to the left.
     */
    protected abstract void turnLeft();

    /**
     * Receives a list of commands such as: 'f' (moveForward), 'b' (moveBackward), 'l' (turnLeft), and 'r' (turnRight).
     * Upon executing the commands, it changes the position or orientation of the rover.
     * If a sequence of commands causes the rover to encounter an obstacle, the execution of commands is halted,
     * and the encountered obstacle is returned.
     *
     * @param commands The list of commands to be executed.
     * @return The encountered obstacle, or null if the sequence completes successfully.
     */
    public abstract Obstacle executeCommands(List<Character> commands);

}















