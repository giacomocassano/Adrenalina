package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ControllerVisitor;

import java.io.Serializable;

/**
 * Empower response is sent from client to server.
 * Client tells server if he wants to use an empower.
 */
public class EmpowerResponse extends EventFromView implements Serializable {

    private EmpowerInfo empower;
    private boolean wantUse;

    /**
     *
     * @param player is client's name
     * @param empower is empower that client want to use
     * @param wantUse is trye if client want to use it
     */
    public EmpowerResponse(String player, EmpowerInfo empower, boolean wantUse){
        super(player);
        this.empower=empower;
        this.wantUse = wantUse;
    }

    /**
     *
     * @return the empower
     */
    public EmpowerInfo getEmpower() {
        return empower;
    }

    /**
     *
     * @return true if client wants to use empower
     */
    public boolean isWantUse() {
        return wantUse;
    }

    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }
}
