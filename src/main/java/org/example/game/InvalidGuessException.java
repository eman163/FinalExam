package org.example.game;

// Required element: class that creates a custom exception that makes sense for the game
public class InvalidGuessException extends Exception {
    public InvalidGuessException(String message) {
        super(message);
    }
}
