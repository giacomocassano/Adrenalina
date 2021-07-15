package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ControllerVisitor;

import java.io.Serializable;
import java.util.List;

/**
 * This is an ultra effect response, sent by client to server to reply at a UltraEffectRequest.
 * Clients with this message tells server if he wants to use an ultra effect
 */
public class UltraEffectResponse  extends EventFromView implements Serializable {

    private boolean isAborted;
    private boolean response;
    private List<EmpowerInfo> payingEmpowers;

    public UltraEffectResponse(String player) {
        super(player);
    }

    /**
     * This message is sent after an UltraEffectRequest.
     * @param player is the name of the player
     * @param isAborted is true if player wants to abort the action
     * @param response is true if he wants to use the ultra effect
     * @param payingEmpowers are empowers that player uses to pay the effect
     */
    public UltraEffectResponse(String player, boolean isAborted, boolean response, List<EmpowerInfo> payingEmpowers) {
        super(player);
        this.isAborted = isAborted;
        this.response = response;
        this.payingEmpowers = payingEmpowers;
    }

    public boolean getResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public List<EmpowerInfo> getPayingEmpowers() {
        return payingEmpowers;
    }

    public void setPayingEmpowers(List<EmpowerInfo> payingEmpowers) {
        this.payingEmpowers = payingEmpowers;
    }

    /**
     *
     * @return true if actions has been aborted.
     */
    public boolean isAborted() {
        return isAborted;
    }

    public void setAborted(boolean aborted) {
        isAborted = aborted;
    }

    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }
}


