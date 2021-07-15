package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;
import it.polimi.ingsw.model.AmmoCubes;

import java.io.Serializable;

/**
 * this requst is sent from server after client's decides to grab an ammocard.
 */
public class GrabAmmoRequest extends EventFromModel implements Serializable {

    private AmmoCubes cubes;
    private boolean hasEmpower;

    /**
     *
     * @param player is player's name
     * @param cubes are cubes that ammo card contains
     * @param hasEmpower is true if ammo card allows to take an empower from empowers deck
     */
    public GrabAmmoRequest(String player, AmmoCubes cubes, boolean hasEmpower) {
        super(player);
        this.cubes = cubes;
        this.hasEmpower = hasEmpower;
    }

    public AmmoCubes getCubes() {
        return cubes;
    }

    public boolean isHasEmpower() {
        return hasEmpower;
    }


    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
