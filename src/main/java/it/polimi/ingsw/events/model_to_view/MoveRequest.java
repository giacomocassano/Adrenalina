package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;
import it.polimi.ingsw.model.MoveType;

import java.io.Serializable;
import java.util.List;

/**
 * This request is sent from server to clients when their turn starts.
 *Client has to choose one valid move from the list.
 */
public class MoveRequest extends EventFromModel implements Serializable {

    private List<MoveType> validMoves;
    private boolean isFinalFrenzy;
    private int actionNumber;
    private int moveNumber;

    /**
     * Constructor
     * @param isFinalFrenzy is a boolean, true if game is on final frenzy mode
     * @param activePlayer is the name of the player that recives the request
     * @param validMoves is a list of valid moves that player can do
     * @param actionNumber is the number of the action that player is doing
     * @param moveNumber is the number of the move that player is doing
     */
    public MoveRequest(boolean isFinalFrenzy, String activePlayer, List<MoveType> validMoves, int actionNumber, int moveNumber) {
        super(activePlayer);
        this.isFinalFrenzy = isFinalFrenzy;
        this.validMoves = validMoves;
        this.actionNumber = actionNumber;
        this.moveNumber = moveNumber;
    }

    public List<MoveType> getValidMoves() {
        return validMoves;
    }

    public boolean isFinalFrenzy() {
        return isFinalFrenzy;
    }

    public int getActionNumber() {
        return actionNumber;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
