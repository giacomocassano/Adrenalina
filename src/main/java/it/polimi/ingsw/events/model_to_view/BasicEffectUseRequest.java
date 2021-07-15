package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This message is the request sent by the server to a client after a Basic Effect request"
 * playerNames is a List of shootable targets
 * maxPlayers is the number of max players selectable
 * maxSquares is the number of max squares selectable
 * squarePositions is a List of shootable squares
 * recoilPositions is a List of Positions to recoil the enemies.
 */

public class BasicEffectUseRequest extends EventFromModel implements Serializable {

    private List<String> playerNames;
    private List<Position> squarePositions;
    private int maxPlayers;
    private int maxSquares;
    private List<Position> recoilPositions;

    /**
     * Default constructor.
     */
    public BasicEffectUseRequest() {
        playerNames = new ArrayList<>();
        squarePositions = new ArrayList<>();
        recoilPositions = new ArrayList<>();
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(List<String> playerNames) {
        this.playerNames = playerNames;
    }

    public List<Position> getSquarePositions() {
        return squarePositions;
    }

    public void setSquarePositions(List<Position> squarePositions) {
        this.squarePositions = squarePositions;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMaxSquares() {
        return maxSquares;
    }

    public void setMaxSquares(int maxSquares) {
        this.maxSquares = maxSquares;
    }

    public List<Position> getRecoilPositions() {
        return recoilPositions;
    }

    public void setRecoilPositions(List<Position> recoilPositions) {
        this.recoilPositions = recoilPositions;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
