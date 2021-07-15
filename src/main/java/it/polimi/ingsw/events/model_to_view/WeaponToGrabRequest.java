package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.events.visitors.ViewVisitor;
import it.polimi.ingsw.model.AmmoCubes;

import java.util.List;

/**
 * This is a request sent from server to client when a player in a spawn point,
 * decides to grab a weapon.
 * WeaponsToGrab is a list of weapons that player can grab
 * WeaponsToDrop is a list of weapons that player can drop
 * ammocubes are player's cubes
 * paying empowers are player's empowers.
 */
public class WeaponToGrabRequest extends EventFromModel{

    private List<WeaponInfo> weaponsToGrab;
    private List<WeaponInfo> weaponsToDrop;
    private AmmoCubes ammoCubes;
    private List<EmpowerInfo> payingEmpowers;

    /**
     * Constructor.
     */
    public WeaponToGrabRequest() {
    }

    /**
     * Sets weapons player can grab
     * @param weaponsToGrab are weapons player can grab
     */
    public void setWeaponsToGrab(List<WeaponInfo> weaponsToGrab) {
        this.weaponsToGrab = weaponsToGrab;
    }

    /**
     * Sets weapon player can drop
     * @param weaponsToDrop are weapons player can drop
     */
    public void setWeaponsToDrop(List<WeaponInfo> weaponsToDrop) {
        this.weaponsToDrop = weaponsToDrop;
    }

    /**
     * gets ammocubes (cost of the weapon)
     * @return cost of the weapon in cubes
     */
    public AmmoCubes getAmmoCubes() {
        return ammoCubes;
    }

    /**
     * Sets cost of the weapon in cubes
     * @param ammoCubes is cost of the weapon
     */
    public void setAmmoCubes(AmmoCubes ammoCubes) {
        this.ammoCubes = ammoCubes;
    }

    /**
     * @return empowers usable by player to pay
     */
    public List<EmpowerInfo> getPayingEmpowers() {
        return payingEmpowers;
    }

    /**
     * @param payingEmpowers are empowers that player can use to pay
     */
    public void setPayingEmpowers(List<EmpowerInfo> payingEmpowers) {
        this.payingEmpowers = payingEmpowers;
    }

    /**
     * @return weapons that player can grab
     */
    public List<WeaponInfo> getWeaponsToGrab() {
        return weaponsToGrab;
    }

    /**
     *
     * @return weapons that player can drop
     */
    public List<WeaponInfo> getWeaponsToDrop() {
        return weaponsToDrop;
    }


    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
