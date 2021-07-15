package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.events.visitors.ControllerVisitor;

import java.io.Serializable;
import java.util.List;

/**
 * This response is sent from client to server after a WeaponToReloadRequest
 * client sends to server every info about a reload action. Client sends to server
 * a list of weapons to reload and a list of empowers to pay reloads costs.
 */

public class WeaponsToReloadResponse extends EventFromView implements Serializable {

    private List<WeaponInfo> weaponsToReload;
    private List<EmpowerInfo> payingEmpowers;
    private boolean isAborted;

    /**
     *
     * @param player is client's name (player's name)
     * @param weaponsToReload are weapons that player wants to reload
     * @param payingEmpowers are empowers that player uses to reload
     * @param isAborted is true if player doesn't want to reload anymore.
     */
    public WeaponsToReloadResponse(String player, List<WeaponInfo> weaponsToReload, List<EmpowerInfo> payingEmpowers, boolean isAborted) {
        super(player);
        this.weaponsToReload = weaponsToReload;
        this.payingEmpowers = payingEmpowers;
        this.isAborted = isAborted;
    }

    public WeaponsToReloadResponse(String player) {
        super(player);
    }

    public List<WeaponInfo> getWeaponsToReload() {
        return weaponsToReload;
    }

    public void setWeaponsToReload(List<WeaponInfo> weaponsToReload) {
        this.weaponsToReload = weaponsToReload;
    }

    public List<EmpowerInfo> getPayingEmpowers() {
        return payingEmpowers;
    }

    public void setPayingEmpowers(List<EmpowerInfo> payingEmpowers) {
        this.payingEmpowers = payingEmpowers;
    }

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
