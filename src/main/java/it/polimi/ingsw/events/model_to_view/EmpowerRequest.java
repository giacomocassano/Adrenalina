package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;
import java.util.List;

/**
 * Empower request is sent from server to client to ask him if he wants to use
 * an empower or not.
 */
public class EmpowerRequest extends EventFromModel implements Serializable {

    private List<EmpowerInfo> validEmpowers;

    public EmpowerRequest() {}

    /**
     * Constructor
     * @param player is player's name (client's name)
     * @param validEmpowers is a list of empowers that client/player can use.
     */
    public EmpowerRequest(String player, List<EmpowerInfo> validEmpowers) {
        super(player);
        this.validEmpowers = validEmpowers;
    }


    public List<EmpowerInfo> getValidEmpowers() {
        return validEmpowers;
    }

    public void setValidEmpowers(List<EmpowerInfo> validEmpowers) {
        this.validEmpowers = validEmpowers;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
