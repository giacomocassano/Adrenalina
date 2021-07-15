package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;
import java.util.List;

/**
 * This is a reconnection notification that server sends to clients
 * when a disconnected player reconnects.
 */
public class ReconnectionNotification extends EventFromModel implements Serializable {
    private String reconnectedPlayer;
    private List<String> connectedPlayers;

    /**
     *
     * @param reconnectedPlayer is the disconnected player that has reconnected
     * @param connectedPlayers are connected players
     */
    public ReconnectionNotification(String reconnectedPlayer, List<String> connectedPlayers) {
        this.reconnectedPlayer = reconnectedPlayer;
        this.connectedPlayers = connectedPlayers;
        this.setUpdateEvent(true);
    }

    public String getReconnectedPlayer() {
        return reconnectedPlayer;
    }

    public List<String> getConnectedPlayers() {
        return connectedPlayers;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
