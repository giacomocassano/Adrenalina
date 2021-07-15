package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.events.visitors.ControllerVisitor;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;
import java.util.List;

/**
 * This request is sent from client to server after a BasicEffectUseRequest.
 * Client sends to server which enemy player/client he wants to hit,
 * in which position he wants to recoil an enemy or in which position he wants to
 * do an area damage
 */
public class BasicEffectUseResponse extends EventFromView implements Serializable {

    private List<String> playerNames;
    private List<Position> squarePositions;
    private Position recoilPosition;
    private boolean isAborted;

    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }

    /**
     *
     * @return player's name. (client character's name)
     */
    public List<String> getPlayerNames() {
        return playerNames;
    }

    /**
     *
     * @return square positions.
     */
    public List<Position> getSquarePositions() {
        return squarePositions;
    }

    /**
     *
     * @return the recoil positions
     */
    public Position getRecoilPosition() {
        return recoilPosition;
    }


    /**
     *
     * @return true if client has aborted
     */
    public boolean isAborted() {
        return isAborted;
    }

    /**
     *
     * @param aborted is true if clients wants to abort
     */
    public void setAborted(boolean aborted) {
        isAborted = aborted;
    }

    /**
     *
     * @param isAborted if client wants to abort the action.
     * @param player is client's name. (the shooter)
     * @param playerNames are targets names
     * @param squarePositions are positions chosen if effect is area-based.
     * @param recoilPosition is the position to recoil an enemy
     */
    public BasicEffectUseResponse(boolean isAborted,String player, List<String> playerNames, List<Position> squarePositions, Position recoilPosition) {
        super(player);
        this.isAborted=isAborted;
        this.playerNames = playerNames;
        this.squarePositions = squarePositions;
        this.recoilPosition = recoilPosition;
    }
}
