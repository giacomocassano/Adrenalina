package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * RecoilMovement is a movement effect that allows the shooter to recoil his target to a certain position.
 */
public class RecoilMovement extends Movement {

    private static final String RECOIL_MOVEMENT_NOT_VALID_MESSAGE = "L'effetto di rinculo non è valido!";

    /**
     * Constructor
     * @param maxSteps are max steps that player that use the effect can move.
     */
    public RecoilMovement(int maxSteps) {
        super("", "", new AmmoCubes(0,0,0), maxSteps);
    }


    /**
     * Checks if effect is valid, so there is no null value, if player and targets are in the same line
     * and other controls.
     * @param map is the playing map. Is used by the method to see if effect can be performed.
     * @return true if effect is valid, so is ready to be performed.
     */
    @Override
    public boolean isValidEffect(Map map) {
        if(getPlayer() == null || map == null) return false;
        if(!map.getPlayersOnMap().contains(getPlayer())) return true;
        List<Square> squareList = new ArrayList<>();
        squareList.add(getPosition());
        if(!map.checkTheLine(map.getPlayerPosition(getPlayer()), squareList, 0, getMaxSteps()))
            return false;
        int distance = map.getSquareDistance(map.getPlayerPosition(getPlayer()), getPosition());
        if(distance > getMaxSteps())
            return false;
        return true;
    }

    /**
     * Performs the effect
     * @param map is the playing map and is used to perform the effect.
     * @throws EffectException if effect is not valid, like if there is a null value.
     */
    @Override
    public void performEffect(Map map) throws EffectException {
        if(!isValidEffect(map)) throw new EffectException(RECOIL_MOVEMENT_NOT_VALID_MESSAGE);
        if(map.getPlayersOnMap().contains(getPlayer()))
            map.movePlayer(getPlayer(), getPosition());
    }

}
