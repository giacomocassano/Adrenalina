package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;
import java.util.List;

/**
 * This is a request sent by server to client when he's dead and he has to respawn his
 * character in a spawn point
 */
public class RespawnRequest extends EventFromModel implements Serializable {

    private List<EmpowerInfo> empowers;

    public RespawnRequest() {
    }

    /**
     *
     * @param player is player's name.
     * @param empowers is a list of empowers, player has to chose one of them and
     *                 discard it.
     */
    public RespawnRequest(String player, List<EmpowerInfo> empowers) {
        super(player);
        this.empowers = empowers;
    }

    public List<EmpowerInfo> getEmpowers() {
        return empowers;
    }

    public void setEmpowers(List<EmpowerInfo> empowers) {
        this.empowers = empowers;
    }



    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
