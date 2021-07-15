package it.polimi.ingsw.exceptions;

/**
 * Exception throw when timer exceeds.
 */
public class TimeExceededException extends Exception {

    /**
     * Costructor.
     * @param message is a description error message.
     */
    public TimeExceededException(String message) {
        super(message);
    }

    /**
     * Default constructor.
     */
    public TimeExceededException() {
    }
}
