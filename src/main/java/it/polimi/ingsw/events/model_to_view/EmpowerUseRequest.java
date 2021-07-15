package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ViewVisitor;
import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a request sent from server to client after an EmpowerRequest.
 * Client want to use an empower so server sends him everything that client can do
 *  with that empower.
 *  Possible targets are targets that player can hit with the empower
 *  Possible Position are position where client can move using the empower
 *  payable is true if empower has a cost
 *  AmmoCubes are player's cubes
 *  paying empowers are player's empowers.
 */
public class EmpowerUseRequest extends EventFromModel implements Serializable {

    private EmpowerInfo empower;
    private List<String> possibleTargets;
    private List<Position> possiblePositions;
    private boolean payable;
    private AmmoCubes ammoCubes;
    private List<EmpowerInfo> payingEmpowers;

    /**
     * Constructor
     * @param player is player's name.
     */
    public EmpowerUseRequest(String player) {
        super(player);
        possibleTargets = new ArrayList<>();
        possiblePositions = new ArrayList<>();
        payingEmpowers = new ArrayList<>();
    }

    public EmpowerInfo getEmpower() {
        return empower;
    }

    public void setEmpower(EmpowerInfo empower) {
        this.empower = empower;
    }

    public List<String> getPossibleTargets() {
        return possibleTargets;
    }

    public void setPossibleTargets(List<String> possibleTargets) {
        this.possibleTargets = possibleTargets;
    }

    public List<Position> getPossiblePositions() {
        return possiblePositions;
    }

    public void setPossiblePositions(List<Position> possiblePositions) {
        this.possiblePositions = possiblePositions;
    }

    public boolean isPayable() {
        return payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    public List<EmpowerInfo> getPayingEmpowers() {
        return payingEmpowers;
    }

    public void setPayingEmpowers(List<EmpowerInfo> payingEmpowers) {
        this.payingEmpowers = payingEmpowers;
    }

    public AmmoCubes getAmmoCubes() {
        return ammoCubes;
    }

    public void setAmmoCubes(AmmoCubes ammoCubes) {
        this.ammoCubes = ammoCubes;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
