package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.client.user_interfaces.model_representation.EffectInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ViewVisitor;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;
import java.util.List;


/**
 * A shooter movement request is a request sent by server to clients when he uses a
 * weapon that allows the shooter to move
 */
public class ShooterMovementRequest extends EventFromModel implements Serializable {

    private EffectInfo movement;
    private List<Position> possiblePositions;
    private List<EmpowerInfo> payingEmpowers;

    /**
     *
     * @param player is player's name
     * @param movement is the movement effect
     * @param possiblePositions are possible positions where player can move
     * @param payingEmpowers are player empowers, player has to pay the effect sometimes.
     */
    public ShooterMovementRequest(String player, EffectInfo movement, List<Position> possiblePositions, List<EmpowerInfo> payingEmpowers) {
        super(player);
        this.movement = movement;
        this.possiblePositions = possiblePositions;
        this.payingEmpowers = payingEmpowers;
    }

    public EffectInfo getMovement() {
        return movement;
    }

    public List<Position> getPossiblePositions() {
        return possiblePositions;
    }

    public List<EmpowerInfo> getPayingEmpowers() {
        return payingEmpowers;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
