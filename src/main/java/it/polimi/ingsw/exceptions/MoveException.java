package it.polimi.ingsw.exceptions;

/**
 * Exception throw when a move is not correctly set.
 */
public class MoveException extends Exception {

    /**
     * Default constructor.
     */
    public MoveException(){
        super();
    }

    /**
     * Constructor.
     * @param message is a description error message.
     */
    public MoveException(String message){

        super(message);

    }
}
