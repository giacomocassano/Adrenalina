package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This Enum  is used to describe a color propriety that is possible
 * to find in some entities in Model, like weapons, empowers, ammo cubes.
 */
public enum Color implements Serializable {
    RED("ROSSO"),
    BLUE("BLU"),
    YELLOW("GIALLO"),
    WHITE("BIANCO"),
    GREY("GRIGIO"),
    PURPLE("VIOLA"),
    GREEN("VERDE");

    private String description;

    Color(String description) {
        this.description = description;
    }

    /**
     * Description getter
     * @return color's description.
     */
    public String getDescription() {
        return description;
    }
}