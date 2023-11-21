package com.martinm1500.marsrover.exceptions;

public class RoverNotFoundException extends RuntimeException{
    public RoverNotFoundException(String message) {
        super(message);
    }
}
