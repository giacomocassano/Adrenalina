package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.events.visitors.ControllerVisitor;
import it.polimi.ingsw.model.Position;
import javafx.geometry.Pos;

import java.io.Serializable;
import java.util.List;

/**
 * This response is sent from client to server after an UltraEffectUseRequest.
 * Client tells server if which players or which positions he wants to hit with weapon's ultra effect,
 * and if he wants to abort or not the effect
 */
public class UltraEffectUseResponse extends EventFromView implements Serializable {

    private List<String> playerNames;
    private Position position;
    private boolean isAborted;

    /**
     *
     * @param isAborted is true if client wants to abort the action
     * @param player is client's name
     * @param playerNames are enemies names that client wants to hit
     * @param position are position that client wants to hit
     */
    public UltraEffectUseResponse(boolean isAborted,String player, List<String> playerNames, Position position) {
        super(player);
        this.isAborted=isAborted;
        this.playerNames = playerNames;
        this.position = position;
    }

    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isAborted() {
        return isAborted;
    }

    public void setAborted(boolean aborted) {
        isAborted = aborted;
    }


}
