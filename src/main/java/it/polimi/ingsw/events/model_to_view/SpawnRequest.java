package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;
import java.util.List;

/**
 * This is a spawn request sent by server to a client when he's spawning
 * the first time.
 */
public class SpawnRequest extends EventFromModel implements Serializable {

    private List<EmpowerInfo> empowers;

    public SpawnRequest() {
    }

    /**
     *
     * @param player is player's name
     * @param empowers is a list of empowers. Player has to choose
     * one of them and discard it. Then player spawns in empower's color respawn-point
     */
    public SpawnRequest(String player, List<EmpowerInfo> empowers) {
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
