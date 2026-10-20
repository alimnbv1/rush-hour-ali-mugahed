package hu.nye.rushhour.persistence;

/**
 * Thrown when a board text file has invalid content.
 */
public class BoardFormatException extends Exception {

    public BoardFormatException(String message) {
        super(message);
    }
}
