package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Square;

import java.util.List;

/**
 * A shooter movement effect is an effect that allows a movement of the player that is using the effect.
 * (the shooter) Effect can be performed first or after a base effect is some weapons
 */
public class ShooterMovement extends Movement {

    private static final String NOT_VALID_MOVEMENT_MESSAGE = "Movimento non valido!";
    private int order;

    /**
     * Constructor
     * @param name is name of effect
     * @param description is the description of effect
     * @param cost is the cost of effect based in cubes
     * @param maxSteps are max steps that player can move.
     */
    public ShooterMovement(String name, String description, AmmoCubes cost, int maxSteps) {
        super(name, description, cost, maxSteps);
        this.order = 0;
    }

    /**
     * Checks if effect is valid, like if there is no null value
     * @param map is the playing map. Is used by the method to see if effect can be performed.
     * @return true if the effect is valid
     */
    @Override
    public boolean isValidEffect(Map map) {
        if(getPlayer() == null || getPosition() == null || map == null) return false;
        int distance = map.getSquareDistance(map.getPlayerPosition(getPlayer()), getPosition());
        if(distance > getMaxSteps() ) return false;
        return true;
    }

    /**
     * Performs the effect if it is valid
     * @param map is the playing map and is used to perform the effect.
     * @throws EffectException if effect is not valid, like if there is a null value
     */
    @Override
    public void performEffect(Map map) throws EffectException {
        if(!isValidEffect(map)) throw  new EffectException(NOT_VALID_MOVEMENT_MESSAGE);
        map.movePlayer(getPlayer(), getPosition());
    }

    /**
     * order flag getter
     * @return order flag
     */
    public int getOrder() {
        return order;
    }

    /**
     * order flag setter
     * @param order flag
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Gets a list of possible movement squares
     * @param map is the playing map
     * @return a list of possible squares where player can move.
     */
    public List<Square> getPossibleMovementSquare(Map map) {
        return map.getSquareAtMaxDistance(map.getPlayerPosition(getPlayer()), getMaxSteps());
    }
}
