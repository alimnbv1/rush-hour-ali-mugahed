package hu.nye.rushhour.service;

/**
 * Thrown when a move cannot be applied to the board.
 */
public class InvalidMoveException extends Exception {

    public InvalidMoveException(String message) {
        super(message);
    }
}
