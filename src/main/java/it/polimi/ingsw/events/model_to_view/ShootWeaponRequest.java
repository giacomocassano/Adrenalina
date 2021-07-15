package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;
import java.util.List;

/**
 * This is a request sent by server to client when client decides to
 * use a weapon to shoot someone during his turn.
 */
public class ShootWeaponRequest extends EventFromModel implements Serializable {

    private List<WeaponInfo> weapons;

    /**
     *
     * @param player is player's name
     * @param weapons is a list of valid weapons that client can use.
     */
    public ShootWeaponRequest(String player, List<WeaponInfo> weapons) {
        super(player);
        this.weapons = weapons;
    }

    public List<WeaponInfo> getWeapons() {
        return weapons;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
