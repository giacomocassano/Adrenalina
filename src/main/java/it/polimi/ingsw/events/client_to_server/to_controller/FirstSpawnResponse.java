package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ControllerVisitor;

import java.io.Serializable;
import java.util.List;

/**
 * this response is sent from client to server to spawn the first time after a SpawnRequest.
 * Client tells server where he want to spawn sending him the discarded empower.
 */
public class FirstSpawnResponse extends EventFromView implements Serializable {

    private EmpowerInfo droppedEmpower;
    private List<EmpowerInfo> otherEmpower;

    public FirstSpawnResponse() {
    }

    /**
     *
     * @param player is client's character name
     * @param droppedEmpower is empower discarded by player
     * @param otherEmpower is empower that player decides to take.
     */
    public FirstSpawnResponse(String player, EmpowerInfo droppedEmpower, List<EmpowerInfo> otherEmpower) {
        super(player);
        this.droppedEmpower = droppedEmpower;
        this.otherEmpower = otherEmpower;
    }

    /**
     *
     * @return empower dropped by player
     */
    public EmpowerInfo getDroppedEmpower() {
        return droppedEmpower;
    }

    /**
     *
     * @param droppedEmpower is empower dropped by player
     */
    public void setDroppedEmpower(EmpowerInfo droppedEmpower) {
        this.droppedEmpower = droppedEmpower;
    }


    /**
     * Sets other empower
     * @param otherEmpower is the empower that player has not discarded.
     */
    public void setOtherEmpower(List<EmpowerInfo> otherEmpower) {
        this.otherEmpower = otherEmpower;
    }

    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }
}
