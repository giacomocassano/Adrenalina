package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.events.client_to_server.ServerEvent;
import it.polimi.ingsw.events.visitors.ControllerVisitor;
import it.polimi.ingsw.events.visitors.ServerVisitor;

import java.io.Serializable;

/**
 * This abstract class is the skeleton of every event from View (clients) to server.
 * it contains just player name.
 */
public abstract class EventFromView extends ServerEvent implements Serializable {

    private String player;

    /**
     * Default constructor.
     */
    public EventFromView() {
    }

    public EventFromView(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void acceptServerVisitor(ServerVisitor visitor) {
        visitor.visit(this);
    }

    public abstract void acceptControllerVisitor(ControllerVisitor visitor);
}
