package it.polimi.ingsw.exceptions;

/**
 * Exception throw when an effect is not correctly set.
 */
public class EffectException extends Exception {

    /**
     * Default constructor.
     */
    public EffectException(){super();}

    /**
     * Constructor.
     * @param message is a description error message.
     */
    public EffectException(String message){
        super(message);
    }

}
