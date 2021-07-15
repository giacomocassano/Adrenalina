package it.polimi.ingsw.events.client_to_server.to_controller;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.events.visitors.ControllerVisitor;

import java.io.Serializable;
import java.util.List;


/**
 * This message is sent from client to server after a WeaponToGrabRequest
 * In this response server can find all infos about a move action that involves grabbing a weapon
 */
public class WeaponToGrabResponse extends EventFromView implements Serializable {

    private WeaponInfo gainedWeapon;
    private WeaponInfo droppedWeapon;
    private List<EmpowerInfo> payingEmpowers;
    private boolean isAborted;

    /**
     *
     * @param player is player's name
     * @param gainedWeapon is the weapon that player wants to grab
     * @param droppedWeapon is the weapon that player wants to drop
     * @param payingEmpowers are empowers that player used to pay the weapon
     * @param isAborted is true if player doesn't want to grab the weapon anymore
     */
    public WeaponToGrabResponse(String player,WeaponInfo gainedWeapon,WeaponInfo droppedWeapon, List<EmpowerInfo> payingEmpowers,boolean isAborted) {
        super(player);
        this.droppedWeapon=droppedWeapon;
        this.gainedWeapon=gainedWeapon;
        this.payingEmpowers=payingEmpowers;
        this.isAborted=isAborted;
    }

    public WeaponInfo getGainedWeapon() {
        return gainedWeapon;
    }

    public void setGainedWeapon(WeaponInfo gainedWeapon) {
        this.gainedWeapon = gainedWeapon;
    }

    public WeaponInfo getDroppedWeapon() {
        return droppedWeapon;
    }

    public void setDroppedWeapon(WeaponInfo droppedWeapon) {
        this.droppedWeapon = droppedWeapon;
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
