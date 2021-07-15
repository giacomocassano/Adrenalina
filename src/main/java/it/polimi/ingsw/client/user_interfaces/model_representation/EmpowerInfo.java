package it.polimi.ingsw.client.user_interfaces.model_representation;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.cards.EmpowerType;

import java.io.Serializable;

/**
 * This class is used to give to Clients a representation about an Empower-Card.
 * It has only getters and setters. There is no logic inside this class.
 * More information look Empoewer Card in Model
 */


public class EmpowerInfo implements Serializable {

    private int id;
    private String name;
    private EmpowerType type;
    private String description;
    private Color color;

    /**
     * Default Constructor.
     */
    public EmpowerInfo() {
    }

    /**
     * Id Getter
     * @return the id of the Empower Card.
     */
    public int getId() {
        return id;
    }

    /**
     * Id Setter
     * @param id is the id of the Empower Card. Used to display it in GUI/CLI
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Name getter
     * @return emp's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Name Setter.
     * @param name is emp's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Type Getter
     * @return empower's type.
     */
    public EmpowerType getType() {
        return type;
    }

    /**
     * Type Setter
     * @param type is empower's type. Look at ENUM
     */
    public void setType(EmpowerType type) {
        this.type = type;
    }

    /**
     * Description Getter
     * @return emp's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Descriprion setter.
     * @param description is empower's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Color getter
     * @return empower's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Color setter
     * @param color is empower's color
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
