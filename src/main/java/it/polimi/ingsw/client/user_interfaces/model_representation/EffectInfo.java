package it.polimi.ingsw.client.user_interfaces.model_representation;

import it.polimi.ingsw.model.AmmoCubes;

import java.io.Serializable;

/**
 * This class is used to give to Clients a representation about a weapon-effect.
 * It has only getters and setters. There is no logic inside this class.
 * More information are in Effect package in Model package.
 */

public class EffectInfo implements Serializable {
    private String name;
    private AmmoCubes cost;
    private String description;

    /**
     * This is the base class constructor.
     */
    public EffectInfo() {
    }

    /**
     *Name getter
     * @return Effect's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter.
     * @param name is effect's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * AmmoCubes getter
     * @return the cost of the effect.
     */

    public AmmoCubes getCost() {
        return cost;
    }

    /**
     * Cost setter.
     * @param cost are AmmoCubes that must be payed to use effect.
     */
    public void setCost(AmmoCubes cost) {
        this.cost = cost;
    }

    /**
     * Description Getter
     * @return Description of the effect
     */
    public String getDescription() {
        return description;
    }
    /**
     * Cost setter.
     * @param description is the descriprion of the effect.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
