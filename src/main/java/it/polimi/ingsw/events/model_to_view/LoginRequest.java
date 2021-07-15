package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;

/**
 * This is the first event sent from server to clients during the login.
 */
public class LoginRequest extends EventFromModel implements Serializable {

    public LoginRequest() {
        this.setUpdateEvent(true);
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
