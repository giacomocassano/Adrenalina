package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.events.visitors.ControllerVisitor;
import it.polimi.ingsw.model.MoveType;

import java.io.Serializable;

/**
 * This is client's response to MoveRequest event sent by server.
 * Clients tells server which move he wants to do in the active action.
 */
public class MoveResponse extends EventFromView implements Serializable {

    private MoveType moveType;

    /**
     *
     * @param player is client's name.
     * @param moveType is type of the move.
     */
    public MoveResponse(String player, MoveType moveType) {
        super(player);
        this.moveType = moveType;
    }

    public MoveType getMoveType() {
        return moveType;
    }


    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }
}
