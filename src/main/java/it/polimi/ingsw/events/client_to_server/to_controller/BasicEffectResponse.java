package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.client.user_interfaces.model_representation.EffectInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ControllerVisitor;

import java.io.Serializable;
import java.util.List;

/**
 * This is a response (event) that client send to server after a BasicEffectRequest
 * With this response, client tells to server if he wants to use a weapon's effect or not.
 */

public class BasicEffectResponse extends EventFromView implements Serializable {

    private boolean isAborted;
    private EffectInfo effectInfo;
    private List<EmpowerInfo> payingEmpowers;

    /**
     * Default constructor
     * @param player is name of client's character
     */
    public BasicEffectResponse(String player) {
        super(player);
    }

    /**
     *
     * @param player is name of client's character
     * @param isAborted if client wants to abort the effect use
     * @param effectInfo is the chosen effect
     * @param payingEmpowers are empowers that client want to use to pay
     */
    public BasicEffectResponse(String player, boolean isAborted, EffectInfo effectInfo, List<EmpowerInfo> payingEmpowers) {
        super(player);
        this.isAborted = isAborted;
        this.effectInfo = effectInfo;
        this.payingEmpowers = payingEmpowers;
    }

    /**
     *
     * @return the effect
     */
    public EffectInfo getEffectInfo() {
        return effectInfo;
    }

    /**
     *
     * @return paying empowers
     */
    public List<EmpowerInfo> getPayingEmpowers() {
        return payingEmpowers;
    }

    /**
     * Empower's setter.
     * @param payingEmpowers are empowers that client want to use to pay
     */
    public void setPayingEmpowers(List<EmpowerInfo> payingEmpowers) {
        this.payingEmpowers = payingEmpowers;
    }

    /**
     *
     * @return true if client want to abort the action.
     */
    public boolean isAborted() {
        return isAborted;
    }

    /**
     *
     * @param aborted is used in case player wants to abort the action.
     */
    public void setAborted(boolean aborted) {
        isAborted = aborted;
    }

    @Override
    public void acceptControllerVisitor(ControllerVisitor visitor) {
        visitor.visit(this);
    }
}

