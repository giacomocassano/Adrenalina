package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.events.visitors.ControllerVisitor;

/**
 * This is an auto-event sent from server to himself when
 * client's time to respawn is over.
 * With this event server respawns the disconnected client.
 *
 */
public class DeathTimeExceeded extends EventFromView {

    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }
}

