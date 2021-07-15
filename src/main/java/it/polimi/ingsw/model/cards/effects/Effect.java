package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.client.user_interfaces.model_representation.EffectInfo;
import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Map;

/**
 * This is an abstract class that define a weapon effect.
 * A weapon effect has a name, a description, a cost, can be active or not and deals a
 * number of damages and marks.
 */
public abstract class Effect {

    private String name;
    private String description;
    private AmmoCubes cost;
    private int nDamages;
    private int nMark;
    private boolean active;

    /**
     *Costructor
     * @param name is effect's name
     * @param description is effect's description
     * @param cost is effect's cost based on ammo cubes
     * @param nDamages is the number of damages that effect deals
     * @param nMark is the number of marks that effect deals
     */
    public Effect(String name, String description, AmmoCubes cost, int nDamages, int nMark) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.nDamages = nDamages;
        this.nMark = nMark;
    }

    /**
     * @return effect's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return effect's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return effect's cost. Cost is expressed in AmmoCubes.
     */
    public AmmoCubes getCost() {
        return cost;
    }

    /**
     * @return effect's number of damages.
     */
    public int getnDamages() {
        return nDamages;
    }

    /**
     * @return effect's number of marks.
     */
    public int getnMarks() {
        return nMark;
    }

    /**
     * @return the status of the effect. If the effect is chosen by Player  active:true, else false.
     */
    public boolean isActive() {
        return active;
    }

    /**
     *
     * This method sets @param active on true if effect is chosen by Player.
     * @param active is true if effect is active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     *
     * @param map is the playing map. Is used by the method to see if effect can be performed.
     * @return true if effect can be performed.
     */
    public abstract boolean isValidEffect(Map map);

    /**
     * @param map is the playing map and is used to perform the effect.
     * @throws EffectException if there is an error during perform of the effect.
     */

    public abstract void performEffect(Map map) throws EffectException;

    /**
     * Creates an effectInfo related to the effect
     * More info at EffectInfo class.
     * @return an effectInfo,
     */
    public EffectInfo getEffectInfo() {
        EffectInfo info = new EffectInfo();
        info.setName(name);
        info.setDescription(description);
        info.setCost(cost);
        return info;
    }
}
