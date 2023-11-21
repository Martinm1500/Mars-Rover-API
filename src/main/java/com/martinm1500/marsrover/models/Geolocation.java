package com.martinm1500.marsrover.models;

public interface Geolocation {

    /**
     * Checks if the rover is on the left side of the meridian on Mars.
     *
     * @return true if the rover is on the left side of the meridian, otherwise false.
     */
    boolean onMeridianLeftSide();

    /**
     * Checks if the rover is on the right side of the meridian on Mars.
     *
     * @return true if the rover is on the right side of the meridian, otherwise false.
     */
    boolean onMeridianRightSide();

    /**
     * Checks if the rover is on the North Pole of Mars.
     *
     * @return true if the rover is on the North Pole, otherwise false.
     */
    boolean onNorthPole();

    /**
     * Checks if the rover is on the South Pole of Mars.
     *
     * @return true if the rover is on the South Pole, otherwise false.
     */
    boolean onSouthPole();
}


