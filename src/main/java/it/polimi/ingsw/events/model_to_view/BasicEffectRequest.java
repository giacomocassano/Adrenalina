package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.client.user_interfaces.model_representation.EffectInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.events.visitors.ViewVisitor;
import it.polimi.ingsw.model.AmmoCubes;

import java.io.Serializable;
import java.util.List;

/**
 * This message is the request sent by the server to a client after a Shoot Move"
 * basicEffects are Weapon's Effects. Client must choose one of them.
 * ammoCubes are the cubes that Player can use to pay.
 * payingEmpowers are the empowers that Player can us to pay.
 * shooterMovement is the Movement Effect usable during basing effect in some weapons.
 */

public class BasicEffectRequest extends EventFromModel implements Serializable  {

    private List<EffectInfo> basicEffects;
    private AmmoCubes ammoCubes;
    private List<EmpowerInfo> payingEmpowers;
    private EffectInfo shooterMovement;

    /**
     * Default constructor.
     */
    public BasicEffectRequest() {
    }

    public BasicEffectRequest(String player) {
        super(player);
    }

    public List<EffectInfo> getBasicEffects() {
        return basicEffects;
    }

    public void setBasicEffects(List<EffectInfo> basicEffects) {
        this.basicEffects = basicEffects;
    }

    public List<EmpowerInfo> getPayingEmpowers() {
        return payingEmpowers;
    }

    public void setPayingEmpowers(List<EmpowerInfo> payingEmpowers) {
        this.payingEmpowers = payingEmpowers;
    }

    public AmmoCubes getAmmoCubes() {
        return ammoCubes;
    }

    public void setAmmoCubes(AmmoCubes ammoCubes) {
        this.ammoCubes = ammoCubes;
    }

    public EffectInfo getShooterMovement() {
        return shooterMovement;
    }

    public void setShooterMovement(EffectInfo shooterMovement) {
        this.shooterMovement = shooterMovement;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
