package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.events.visitors.ControllerVisitor;

/**
 * This response is sent from client to server after a WeaponToShootRequest.
 *
 */

public class WeaponToShootResponse extends EventFromView{

    private WeaponInfo weapon;
    private boolean isAborted;

    public WeaponInfo getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponInfo weapon) {
        this.weapon = weapon;
    }

    /**
     *
     * @param player is player's name
     * @param weapon is the weapon that player want to use to shoot
     * @param isAborted is true if player wants to abort the shoot action
     */
    public WeaponToShootResponse(String player, WeaponInfo weapon,boolean isAborted) {
        super(player);
        this.weapon = weapon;
        this.isAborted=isAborted;
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
