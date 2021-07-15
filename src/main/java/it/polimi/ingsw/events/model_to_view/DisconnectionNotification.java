package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;
import java.util.List;

/**
 * This is a Notifications sent from Server to Clients after a disconnection.
 *
 */

public class DisconnectionNotification extends EventFromModel implements Serializable {

    private String disconnectedPlayer;
    private List<String> connectedPlayers;

    public DisconnectionNotification(String disconnectedPlayer) {
        this.disconnectedPlayer = disconnectedPlayer;
        this.setUpdateEvent(true);
    }

    public String getDisconnectedPlayer() {
        return disconnectedPlayer;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
