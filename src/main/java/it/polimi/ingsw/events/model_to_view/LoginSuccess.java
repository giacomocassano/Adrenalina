package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;

/**
 * Login Success is an event sent from server to client when he successfully
 * connects to the server
 *
 */

public class LoginSuccess extends EventFromModel implements Serializable {

    private int type;
    private String nameConfirmed;
    private String welcomeMessage;

    public static final int INITIAL_LOGIN = 1;
    public static final int RECONNECT_SUCCESS = 2;

    /**
     *
     * @param type is 1 if is the first login or 2 if clients has reconnected
     * @param nameConfirmed is client's name (player's name)
     * @param welcomeMessage is a welcome message
     */
    public LoginSuccess(int type, String nameConfirmed, String welcomeMessage) {
        this.nameConfirmed = nameConfirmed;
        this.welcomeMessage = welcomeMessage;
        this.type = type;
        this.setUpdateEvent(true);
    }

    public String getNameConfirmed() {
        return nameConfirmed;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public int getType() {
        return type;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
