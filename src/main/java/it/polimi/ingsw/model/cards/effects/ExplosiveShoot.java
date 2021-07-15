package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Explosive shot is a weapon effect, that hits an enemy which is visible from target
 * and the hits everyone in the same square and give them marks or damages.
 *
 */
public class ExplosiveShoot extends ShootEffect {

    private int areaDamages;
    private int areaMarks;

    private static final int NUM_TARGET = 1;
    private static final String NOT_VALID_EFFECT_MESSAGE = "L'effetto non è valido!";

    /** @param name is effect's name
     * @param description is effect's description
     * @param cost is effect's cost based on ammocubes
     * @param nDamages is max number of damage that effect deals
     * @param nMarks is max number of marks that effect deals
     * @param ultraDamage is the ultra effect related to this one
     * @param areaDamages is the number of squares reachable by effect where targets are damaged
     * @param areaMarks is the number of squares reachable by effect where targets get marked
     */
    public ExplosiveShoot(String name, String description, AmmoCubes cost, int nDamages, int nMarks, int areaDamages, int areaMarks, UltraDamage ultraDamage) {
        super(name, description, cost, false, nDamages, nMarks, true, false, null, ultraDamage);
        this.areaDamages = areaDamages;
        this.areaMarks = areaMarks;
    }

    /**
     * Checks if effect is valid.
     * Checks if target and player are in the same square, and if shooter can see his target.
     * @param map is the playing map. Is used by the method to see if effect can be performed.
     * @return true if effect is valid so is ready to be performed
     */
    @Override
    public boolean isValidEffect(Map map) {
        if(getShooter() == null || getTargets() == null || getTargets().size() != NUM_TARGET) return false;
        Player target = getTargets().get(0);
        Square targetSquare = map.getPlayerPosition(target);
        Square shooterSquare = map.getPlayerPosition(getShooter());
        if(shooterSquare == targetSquare) return false;
        if(!map.getOtherSquare(shooterSquare, true).contains(targetSquare)) return false;
        return true;
    }

    /**
     * Performs the effect.
     * @param map is the playing map and is used to perform the effect.
     * @throws EffectException if there is an error performing this effect or another effect related to this one.
     */
    @Override
    public void performEffect(Map map) throws EffectException {
        if(!isValidEffect(map)) throw new EffectException(NOT_VALID_EFFECT_MESSAGE);
        Player target = getTargets().get(0);
        Square targetSquare = map.getPlayerPosition(target);
        target.sufferDamage(map, getShooter(), getnDamages(), getnMarks());
        List<Player> playersInTargetSquare = targetSquare.getPlayers();
        for(Player t: playersInTargetSquare) {
            t.sufferDamage(map, getShooter(), areaDamages, areaMarks);
        }
        playersInTargetSquare.remove(getShooter());
        getTargets().addAll(playersInTargetSquare);
        if(getUltraDamage() != null) {
            getUltraDamage().setShooter(getShooter());
            getUltraDamage().setTargets(getTargets());
        }
    }

    /**
     *
     * @return area marks value
     */
    public int getAreaMarks() {
        return areaMarks;
    }

    /**
     *
     * @param map is playing map
     * @param shooter is the player that is using the effect
     * @return a list of possible targets, too difficult to calculate in this case
     * so return a list of other players.
     */
    @Override
    public List<Player> getPossibleTargets(Map map, Player shooter) {
        return map.getOtherPlayers(shooter, true);
    }

    /**
     *
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return an empty collection of squares.
     */
    @Override
    public List<Square> getPossibleSquares(Map map, Player shooter) {
        return new ArrayList<>();
    }

    /**
     *
     * @return number of targets to select
     */
    @Override
    public int getNumTargetsToSelect() {
        return NUM_TARGET;
    }

    /**
     *
     * @return number of squares to select
     * 0 in this effect because is a target based effect.
     */
    @Override
    public int getNumSquaresToSelect() {
        return 0;
    }
}
