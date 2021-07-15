package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.events.visitors.ControllerVisitor;

import java.io.Serializable;

public class GrabAmmoResponse extends EventFromView implements Serializable {

    private boolean abort;

    /**
     *
     * @param player is client's name.
     * @param abort is true if player wants to abort the action. (doesn't want
     *              to grab anymore)
     */
    public GrabAmmoResponse(String player, boolean abort) {
        super(player);
        this.abort = abort;
    }

    public boolean isAborted() {
        return abort;
    }

    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }
}
