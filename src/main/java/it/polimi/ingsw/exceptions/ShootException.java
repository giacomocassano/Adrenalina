package it.polimi.ingsw.exceptions;

/**
 * Exception throw when a shoot is not correctly set.
 */
public class ShootException extends Exception {

    /**
     * Default constructor.
     */
    public ShootException(){
        super();
    }

    /**
     * Constructor.
     * @param message is a description error message.
     */
    public ShootException(String message){
        super(message);
    }
}
