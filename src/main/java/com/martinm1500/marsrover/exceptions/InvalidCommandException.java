package com.martinm1500.marsrover.exceptions;

public class InvalidCommandException extends RuntimeException{
    public InvalidCommandException(String message) {
        super(message);
    }
}
