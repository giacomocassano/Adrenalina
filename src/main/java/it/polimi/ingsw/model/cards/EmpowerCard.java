package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.Color.*;

/**
 * This class extends card, is used to represents an empower card.
 * An empower card has a type, a description, a color, a player that use it: user
 * a target, a cost.
 */
public class EmpowerCard extends Card {

    private EmpowerType type;
    private String description;
    private Color color;
    private Player user;
    private Player target;
    private Square position;
    private Color cubeColor;
    private boolean active;

    /**
     *
     * @param type is empower's type
     * @param name is the name of the empower
     * @param id is the id of the card
     * @param description is the description of the card
     * @param color is the color of the card.
     */
    public EmpowerCard(EmpowerType type, String name,int id,String description, Color color) {
        super(name,id);
        this.description = description;
        this.type = type;
        this.color = color;
        this.active = false;
        user = null;
        target = null;
        position = null;
        cubeColor = null;
    }

    /**
     * Checks if an empower can be used by a player in a map.
     * @param map is the playing map
     * @param activePlayer is tha player that is using the empower
     * @return true if player can use this empower
     */
    public boolean isValidEmpowerUse(Map map, Player activePlayer) {
        switch (type){
            case TARGETING_SCOPE:
                if(user == null || target == null || cubeColor == null || !target.isJustShot() || user != activePlayer)
                    return false;
                if((cubeColor == RED && !user.canPay(new AmmoCubes(1,0,0))) ||
                    (cubeColor == Color.BLUE && !user.canPay(new AmmoCubes(0,1,0))) ||
                    (cubeColor == Color.YELLOW && !user.canPay(new AmmoCubes(0, 0, 1))))
                    return false;
                break;
            case NEWTON:
                if(user == null || target == null || position == null || user != activePlayer)
                    return false;
                List<Square> squares = new ArrayList<>();
                squares.add(position);
                if(!map.checkTheLine(map.getPlayerPosition(target), squares, 1, 2) && !map.checkWalls(map.getPlayerPosition(target), position))
                    return false;
                break;
            case TAGBACK_GRANADE:
                if(user == null || target != activePlayer || !user.isJustShot() || !map.getOtherPlayers(user, true).contains(target))
                    return false;
                break;
            case TELEPORTER:
                if(user == null || position == null || user != activePlayer)
                    return false;
                break;
        }
        return true;
    }


    /**
     * Performs the effect of the empower and then discards it.
     * @param map is playing map
     * @param activePlayer is the player that is using the empower
     * @param disc is the discarded stack of empowers.
     * @return true if card's effect is correctly performed.
     */
    public boolean performEmpower(Map map, Player activePlayer, Stack<EmpowerCard> disc) {
        if(!isValidEmpowerUse(map, activePlayer)) return false;
        switch (type) {
            case TARGETING_SCOPE:
                if(cubeColor == RED) user.payCost(new AmmoCubes(1,0,0), disc);
                else if(cubeColor == BLUE) user.payCost(new AmmoCubes(0,1,0), disc);
                else if(cubeColor == YELLOW) user.payCost(new AmmoCubes(0,0,1), disc);
                target.sufferDamage(map, user, 1, 0);
                break;
            case NEWTON:
                map.movePlayer(target, position);
                break;
            case TAGBACK_GRANADE:
                target.sufferDamage(map, user, 0, 1);
                break;
            case TELEPORTER:
                map.movePlayer(user, position);
                break;
        }
        return true;
    }

    /**
     * user setter
     * @param user is the user of the empower
     */
    public void setUser(Player user) {
        this.user = user;
    }

    /**
     * Target setter
     * @param target is empower's target.
     */
    public void setTarget(Player target) {
        this.target = target;
    }

    /**
     * Position setter
     * @param position is empower's position
     */
    public void setPosition(Square position) {
        this.position = position;
    }

    /**
     * cube color setter
     * @param cubeColor is the color to pay the empower
     */
    public void setCubeColor(Color cubeColor) {
        this.cubeColor = cubeColor;
    }

    /**
     * Active status setter
     * @param active is true if empower is active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Type getter
     * @return empower's type
     */
    public EmpowerType getType() {
        return type;
    }

    /**
     * Description getter
     * @return the description of the empower
     */
    public String getDescription() {
        return description;
    }

    /**
     * Color getter
     * @return empower's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * User getter
     * @return player that is using the empower
     */
    public Player getUser() {
        return user;
    }

    /**
     *
     * @return empower's target
     */
    public Player getTarget() {
        return target;
    }

    /**
     *
     * @return empower's position. If empower's effect
     * involves movement
     */
    public Square getPosition() {
        return position;
    }

    /**
     *
     * @return true if empower is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Creates the Empower Info representation from the empower
     * @return the correspondent empowerInfo from the empowerCard
     */
    public EmpowerInfo getEmpowerInfo() {
        EmpowerInfo info = new EmpowerInfo();
        info.setId(getId());
        info.setName(getName());
        info.setType(type);
        info.setDescription(description);
        info.setColor(color);
        return info;
    }
}