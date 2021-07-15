package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;

/**
 * This is a Time exceeded notification sent by server to a client when client's choosing time
 * for a certain action is over.
 */

public class TimeExceededNotification extends EventFromModel implements Serializable {

    private String message;

    public TimeExceededNotification() {
        message = "";
        this.setUpdateEvent(true);
    }

    /**
     * This is the constructor
     * @param message is a message to print or show in client's interface.
     */
    public TimeExceededNotification(String message) {
        this.message = message;
        this.setUpdateEvent(true);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
