package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;
import java.util.List;


/**
 * This is a message sent to clients while they are in the waiting room,
 * if a player tries to connect or disconnect himself, other clients will recive this update.
 */
public class WaitingRoomUpdate extends EventFromModel implements Serializable {
    private List<String> waitingPlayers;
    private String connectedPlayer;
    private String disconnectedPlayer;
    private boolean isStarting;
    private int timer;

    /**
     * This is an update of waiting room
     * @param connectedPlayer is last player connected
     * @param disconnectedPlayer are players disconnected
     * @param waitingPlayers are players waiting for match
     * @param isStarting is true if match is about to begin
     * @param timer is timer before the match starts
     */
    public WaitingRoomUpdate(String connectedPlayer, String disconnectedPlayer, List<String> waitingPlayers, boolean isStarting, int timer) {
        this.connectedPlayer = connectedPlayer;
        this.disconnectedPlayer = disconnectedPlayer;
        this.waitingPlayers = waitingPlayers;
        this.isStarting = isStarting;
        this.timer = timer;
        this.setUpdateEvent(true);
    }

    /**
     *
     * @return waiting players
     */
    public List<String> getWaitingPlayers() {
        return waitingPlayers;
    }

    /**
     *
     * @return last player connected
     */
    public String getConnectedPlayer() {
        return connectedPlayer;
    }

    /**
     *
     * @return disconnected player
     */
    public String getDisconnectedPlayer() {
        return disconnectedPlayer;
    }

    /**
     * @return if game is starting, true.
     */
    public boolean isStarting() {
        return isStarting;
    }

    /**
     *
     * @return time before the game starts
     */
    public int getTimer() {
        return timer;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }


}
