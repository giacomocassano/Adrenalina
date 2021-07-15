package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;

/**
 * This is a message notification sent from server to clients to notify something.
 */
public class MessageNotification extends EventFromModel implements Serializable {

    private final String message;
    private final int type;

    public final static int INFO = 1;
    public final static int ERROR = 2;

    /**
     *
     * @param type is 1 is is an info message or 2 if is an error message
     * @param message is the string to show to client in the interface
     */
    public MessageNotification(int type, String message) {
        this.type = type;
        this.message = message;
        this.setUpdateEvent(true);
    }

    public String getMessage() {
        return message;
    }

    public boolean isInfoMessage() {
        return type == INFO;
    }

    public boolean isErrorMessage() {
        return type == ERROR;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
