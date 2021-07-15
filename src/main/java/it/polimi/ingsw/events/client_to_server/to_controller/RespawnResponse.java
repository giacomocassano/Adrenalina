package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ControllerVisitor;

/**
 * This event is sent after a player's death
 * Client sends this event to server telling him where he wants to respawn his character
 *
 */

public class RespawnResponse extends EventFromView{

    private EmpowerInfo discarded;

    /**
     *
     * @param player is the dead player
     * @param discarded is the empower that he discards to spawn in a spawn point
     */
    public RespawnResponse(String player, EmpowerInfo discarded) {
        super(player);
        this.discarded = discarded;
    }

    public EmpowerInfo getDropped() {
        return discarded;
    }

    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }
}
