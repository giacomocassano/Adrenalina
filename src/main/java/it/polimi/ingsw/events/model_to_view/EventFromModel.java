package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;

/**
 * This is the abstract class extended by every event sent from server to client.
 */
public abstract class EventFromModel implements Serializable {

    private String player;
    private boolean isUpdateEvent;

    public EventFromModel() {
        this.isUpdateEvent = false;
    }

    /**
     *
     * @param player is client's player's name. The name of the character that client plays during a game.
     */
    public EventFromModel(String player) {
        this.player = player;
        this.isUpdateEvent = false;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public boolean isUpdateEvent() {
        return isUpdateEvent;
    }

    public void setUpdateEvent(boolean updateEvent) {
        isUpdateEvent = updateEvent;
    }

    public abstract void acceptViewVisitor(ViewVisitor visitor);
}
