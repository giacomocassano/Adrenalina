package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.events.visitors.ViewVisitor;
import it.polimi.ingsw.model.AmmoCubes;

import java.io.Serializable;
import java.util.List;

/**
 *
 * This message is the request sent by the server to a client.
 * Client can reaload more weapons in one time.
 * "ammoCubes" are the cubes that Player can use to pay.
 * "payingEmpowers" are the empowers that Player can us to pay.
 */
public class WeaponsToReloadRequest extends EventFromModel implements Serializable {

    private List<WeaponInfo> weaponsToReload;
    private AmmoCubes ammoCubes;
    private List<EmpowerInfo> payingEmpowers;

    /**
     * Default costructor.
     */
    public WeaponsToReloadRequest() {
    }

    /**
     *
     * @return weapons that player can reload
     */
    public List<WeaponInfo> getWeaponsToReload() {
        return weaponsToReload;
    }

    /**
     *
     * @param weaponsToReload are weapons that player can reload
     */
    public void setWeaponsToReload(List<WeaponInfo> weaponsToReload) {
        this.weaponsToReload = weaponsToReload;
    }

    /**
     *
     * @return empower that player can use to reload
     */
    public List<EmpowerInfo> getPayingEmpowers() {
        return payingEmpowers;
    }

    /**
     *
     * @param payingEmpowers are empowers that player can use to reload
     */
    public void setPayingEmpowers(List<EmpowerInfo> payingEmpowers) {
        this.payingEmpowers = payingEmpowers;
    }

    /**
     *
     * @return cost of reload expressed in cubes
     */
    public AmmoCubes getAmmoCubes() {
        return ammoCubes;
    }

    /**
     *
     * @param ammoCubes is the reloading cost expressed in cubes
     */
    public void setAmmoCubes(AmmoCubes ammoCubes) {
        this.ammoCubes = ammoCubes;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
