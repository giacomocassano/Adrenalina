package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.events.visitors.ControllerVisitor;

import java.io.Serializable;

/**
 * This event is sent from server to himself if client does not reply to an
 * EmpowerUse Request.
 */
public class EmpowerUseExceeded extends EventFromView implements Serializable {

    /**
     * Default constructor
     */
    public EmpowerUseExceeded() {
    }

    /**
     * This event is sent when time to use an empower is over.
     * @param player is player's name (client's name)
     */
    public EmpowerUseExceeded(String player) {
        super(player);
    }

    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }
}
