package com.martinm1500.marsrover.exceptions;

public class MapNotFoundException extends RuntimeException{
    public MapNotFoundException(String message) {
        super(message);
    }
}
