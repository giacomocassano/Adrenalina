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
 * This is a Vortex Shoot effect, is used by weapons that
 * hit their targets and move them.
 */
public class VortexShoot extends ShootEffect {

    private static final int MAX_TARGETS = 1;
    private static final String NOT_VALID_EFFECT_MESSAGE = "L'effetto non è valido!";

    private int minRange;
    private int maxRange;
    private int targetSteps;

    /**
     *Constructor
     * @param name is effect's name
     * @param description is effect's description
     * @param cost is effect's cost expressed in AmmoCubes
     * @param nDamages is the number of damages that effect deals
     * @param nMarks is the number of targets that effect deals
     * @param ultraDamage is the ultra damage effect related to this one
     * @param minRange is the minimum distance that enemies must be to be hit
     * @param maxRange is the max distance that enemies must be to be hit
     * @param targetSteps is the number of steps that target can be moved.
     */
    public VortexShoot(String name, String description, AmmoCubes cost, int nDamages, int nMarks, UltraDamage ultraDamage, int minRange, int maxRange, int targetSteps) {
        super(name, description, cost, false, nDamages, nMarks, true, true, null, ultraDamage);
        if(minRange == 0 && maxRange == 0) super.setSquareBased(false);
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.targetSteps = targetSteps;
    }

    /**
     * Checks if effect is valid.
     * checks if target distance is between min range and max range, if there is no null value.
     * @param map is the playing map. Is used by the method to see if effect can be performed.
     * @return true if effect is valid.
     */
    @Override
    public boolean isValidEffect(Map map) {
        if(getShooter() == null || getTargets() == null || getTargets().size() != 1)
            return false;
        Square shooterSquare = map.getPlayerPosition(getShooter());
        //If minRange and maxRange are 0, then the vortex square must be the shooter square and the user will not set the square
        if(minRange == 0 && maxRange == 0 && getSquares().isEmpty()) {
            this.getSquares().add(shooterSquare);
        }
        if(getSquares().isEmpty()) return false;
        Square vortexSquare = getSquares().get(0);
        Square targetSquare = map.getPlayerPosition(getTargets().get(0));
        int vortexDistance = map.getSquareDistance(vortexSquare, shooterSquare);
        int targetDistance = map.getSquareDistance(targetSquare, vortexSquare);
        if(vortexDistance < minRange || vortexDistance > maxRange) return false;
        if(targetDistance > targetSteps) return false;
        if(!map.getOtherSquare(shooterSquare, true).contains(vortexSquare)) return false;
        //Set the vortex square in ultra damage
        if(getUltraDamage() != null) {
            getUltraDamage().addSquare(vortexSquare);
        }
        return true;
    }

    /**
     * Performs the effect, move targets and gives him damage.
     * @param map is the playing map and is used to perform the effect.
     * @throws EffectException if effect can't be performed.
     */
    @Override
    public void performEffect(Map map) throws EffectException {
        if(!isValidEffect(map)) throw new EffectException(NOT_VALID_EFFECT_MESSAGE);
        Square vortexSquare = getSquares().get(0);
        Player target = getTargets().get(0);
        target.sufferDamage(map, getShooter(), getnDamages(), getnMarks());
        map.movePlayer(target, vortexSquare);
        if(getUltraDamage() != null) {
            getUltraDamage().setShooter(getShooter());
            getUltraDamage().setTargets(getTargets());
        }
    }

    /**
     * Min range getter
     * @return min range of the effect
     */
    public int getMinRange() {
        return minRange;
    }

    /**
     * Max range getter
     * @return max range of the effect
     */
    public int getMaxRange() {
        return maxRange;
    }

    /**
     *
     * @return target's steps
     */
    public int getTargetSteps() {
        return targetSteps;
    }

    /**
     * Checks for possible targets that shooter can hit using this effect
     * @param map is the playing map.
     * @param shooter is the player that is using this effect
     * @return a list of possible targets
     */
    @Override
    public List<Player> getPossibleTargets(Map map, Player shooter) {
        List<Player> targets = map.getPlayersOnMap();
        targets.remove(shooter);
        if(minRange == 0 && maxRange == 0) {
            targets = targets.stream()
                    .filter(t -> map.getPlayersDistance(shooter, t) <= targetSteps)
                    .collect(Collectors.toList());
        }
        return targets;
    }

    /**
     * Checks for possible squares where player can hit enemies.
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return a list of possible squares
     */
    @Override
    public List<Square> getPossibleSquares(Map map, Player shooter) {
        if(minRange == 0 && maxRange == 0)
            return new ArrayList<>();
        return map.getOtherSquare(map.getPlayerPosition(shooter), true);
    }

    /**
     *
     * @return num of targets that shooter have to select
     */
    @Override
    public int getNumTargetsToSelect() {
        return MAX_TARGETS;
    }

    /**
     *
     * @return num of squares that shooter have to select.
     */
    @Override
    public int getNumSquaresToSelect() {
        if(minRange == 0 && maxRange == 0)
            return 0;
        return 1;
    }
}
