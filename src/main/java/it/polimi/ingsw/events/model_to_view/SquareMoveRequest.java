package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;
import java.util.List;

/**
 * This is a request sent by server to clients when client decides to RUN in a different square
 *
 */
public class SquareMoveRequest extends EventFromModel implements Serializable {

    private List<Position> positions;
    private Position initialPosition;

    /**
     *
     * @param player is player's name
     * @param positions is a list of valid position where player can move
     * @param initialPosition is the starting position of player
     */
    public SquareMoveRequest(String player, List<Position> positions, Position initialPosition) {
        super(player);
        this.positions = positions;
        this.initialPosition = initialPosition;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public Position getInitialPosition() {
        return initialPosition;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
