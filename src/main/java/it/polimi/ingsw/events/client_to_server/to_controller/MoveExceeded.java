package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.events.visitors.ControllerVisitor;

import java.io.Serializable;

/**
 * This event is sent when player's time to do a Move is over.
 * Server sends this event to himself because client doesn't sends a reply
 */
public class MoveExceeded extends EventFromView implements Serializable {

    /**
     * Default constructor.
     */
    public MoveExceeded() {
    }


    public MoveExceeded(String player) {
        super(player);
    }

    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }
}
