package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.events.visitors.ControllerVisitor;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;

/**
 * This event is sent after a SquareToMoveRequest, from client to server.
 * Clients decides a positon where he wants to move his player/character.
 *
 *
 */

public class SquareToMoveResponse extends EventFromView implements Serializable {

    private Position position;
    private boolean isAborted;

    /**
     *
     * @param player is client's player
     * @param position is where he wants to move
     * @param isAborted is true if he wants to abort the action
     */
    public SquareToMoveResponse(String player, Position position,boolean isAborted){
        super(player);
        this.position=position;
        this.isAborted=isAborted;
    }

    /**
     *
     * @return where player wants to move.
     */
    public Position getPosition() {
        return position;
    }

    /**
     *
     * @return true if actions has been aborted.
     */
    public boolean isAborted() {
        return isAborted;
    }

    /**
     * Abort-setter
     * @param aborted is true if player wants to abort this action.
     */
    public void setAborted(boolean aborted) {
        isAborted = aborted;
    }

    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }

}
