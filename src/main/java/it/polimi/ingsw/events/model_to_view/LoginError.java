package it.polimi.ingsw.events.model_to_view;


import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;

/**
 * Login error event is sent by server after a login's fail from a client.
 */
public class LoginError extends EventFromModel implements Serializable {

    public static final int NAME_ALREADY_EXISTING = 1;
    public static final int GAME_ALREADY_STARTED = 2;

    private int type;
    private String nameNotConfirmed;

    /**
     *
     * @param type is 1 if the name already exists or 2 if game already exists
     * @param nameNotConfirmed is client's name that can't be added for some reason.
     */
    public LoginError(int type, String nameNotConfirmed) {
        this.type = type;
        this.nameNotConfirmed = nameNotConfirmed;
        this.setUpdateEvent(true);
    }

    public String getNameNotConfirmed() {
        return nameNotConfirmed;
    }

    public int getType() {
        return type;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
