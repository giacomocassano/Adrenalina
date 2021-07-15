package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Multiple directional shoot is an effect based on area that hits
 * all player in some squares
 */
public class MultipleDirectionalShoot extends ShootEffect {

    private static final int MAX_SQUARE_TO_SELECT = 3;
    private static final String NOT_VALID_EFFECT_MESSAGE = "L'effetto non è valido!";

    private int maxRange;
    private boolean decreased;

    /**
     *
     * @param name is effect's name
     * @param description is effect's description
     * @param cost is effect's cost expressed in ammo cubes
     * @param nDamages is the number of damages that effect deals
     * @param nMarks is the number of marks that effect deals
     * @param ultraDamage is the related ultra damage effect
     * @param maxRange is the max number of squares that target must be to be hit
     * @param decreased is true if in the most distant squares/targets it does less damage
     */
    public MultipleDirectionalShoot(String name, String description, AmmoCubes cost, int nDamages, int nMarks, UltraDamage ultraDamage, int maxRange, boolean decreased) {
        super(name, description, cost, true, nDamages, nMarks, false, true, null, ultraDamage);
        this.maxRange = maxRange;
        this.decreased = decreased;
    }

    /**
     * Checks if effect can be performed
     * Checks if there are no walls and if players are all in the same line
     * @param map is the playing map. Is used by the method to see if effect can be performed.
     * @return true if effect is valid
     */
    @Override
    public boolean isValidEffect(Map map) {
        if(getShooter() == null || getSquares() == null || map == null) return false;
        if(!map.checkTheLine(map.getPlayerPosition(getShooter()), getSquares(), 1, maxRange)) return false;
        for(Square square: getSquares())
            if(!map.checkWalls(map.getPlayerPosition(getShooter()), square)) return false;
        return true;
    }

    /**
     * Performs the effect
     * @param map is the playing map and is used to perform the effect.
     * @throws EffectException if there is an error during the perform of the effect, like a null value.
     */
    @Override
    public void performEffect(Map map) throws EffectException {
        if(!isValidEffect(map)) throw new EffectException(NOT_VALID_EFFECT_MESSAGE);
        if (decreased){
            for(Square square: getSquares()) {
                getTargets().addAll(square.getPlayers());
                int distance = map.getSquareDistance(map.getPlayerPosition(getShooter()), square);
                for(Player target: square.getPlayers())
                    if(target != getShooter())
                        target.sufferDamage(map, getShooter(), getnDamages()+1-distance, 0);
            }
        }else{
            for(Square square: getSquares()) {
                getTargets().addAll(square.getPlayers());
                for (Player target : square.getPlayers())
                    if (target != getShooter())
                        target.sufferDamage(map, getShooter(), getnDamages(), getnMarks());
            }
        }
        if(getUltraDamage() != null) {
            getUltraDamage().setShooter(getShooter());
            getUltraDamage().setTargets(getTargets());
        }
    }

    /**
     *
     * @return max range
     */
    public int getMaxRange() {
        return maxRange;
    }

    /**
     *
     * @return decreased flag
     */
    public boolean isDecreased() {
        return decreased;
    }

    /**
     *
     * @param map is playing map
     * @param shooter is player that is using the effct
     * @return a list of possible targets
     */
    @Override
    public List<Player> getPossibleTargets(Map map, Player shooter) {
        return new ArrayList<>();
    }

    /**
     *
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return a list of possible squares where shooter can hit targets,
     * for sure these squares are not shooter square and squares separated with walls to shooter square.
     */
    @Override
    public List<Square> getPossibleSquares(Map map, Player shooter) {
        Square shPos = map.getPlayerPosition(shooter);
        return map.getOtherSquare(shPos, true).stream()
                .filter(square -> square != shPos)
                .filter(square -> map.checkWalls(shPos, square))
                .collect(Collectors.toList());
    }

    /**
     *
     * @return num of targets to select, 0 in this effect because is a square based effect
     */
    @Override
    public int getNumTargetsToSelect() {
        return 0;
    }

    /**
     *
     * @return num of squares to select
     */
    @Override
    public int getNumSquaresToSelect() {
        return MAX_SQUARE_TO_SELECT;
    }
}
