package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;

/**
 * This is a ping that server sends to clients to see if clients are connected.
 * Is empty.
 */
public class PingClient extends EventFromModel {

    /**
     * Default constructor
     */
    public PingClient() {
        this.setUpdateEvent(true);
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
