package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;
import java.util.List;

/**
 * This message is sent by server to client after he decides to use an Ultra effect with
 * a certain weapon.
 *
 */
public class UltraEffectUseRequest extends EventFromModel implements Serializable {

    private List<String> playerNames;
    private List<Position> squarePositions;
    private int maxPlayers;
    private int maxSquares;

    /**
     * Default Constructor.
     */
    public UltraEffectUseRequest() {
    }

    /**
     *
     * @param playerNames is a List of shootable targets
     * @param maxPlayers is the number of max players selectable
     * @param maxSquares is the number of max squares selectable
     * @param squarePositions is a List of shootable squares
     */
    public UltraEffectUseRequest(List<String> playerNames, List<Position> squarePositions, int maxPlayers, int maxSquares) {
        this.playerNames = playerNames;
        this.squarePositions = squarePositions;
        this.maxPlayers = maxPlayers;
        this.maxSquares = maxSquares;
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

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
