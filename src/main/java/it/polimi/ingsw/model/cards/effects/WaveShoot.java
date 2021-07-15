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
 * WaveShoot effect is a weapon effect.
 * This effect hits everyone in squares at distance 1 from shooter square.
 */
public class WaveShoot extends ShootEffect {

    private int maxTargets;
    private static final String NOT_VALID_EFFECT_MESSAGE = "L'effetto non è valido!";
    private static final int SHOOT_DISTANCE=1;

    /**
     * Constructor
     * @param name is the name of the effect
     * @param description is the description of the effect
     * @param cost is the cost of the effect based in AmmoCubes
     * @param nDamages is the number of damages that the effect deals to targets
     * @param nMarks is the number of marks that the effect deals to targets
     * @param ultraDamage is the related ultra damage.
     * @param maxTargets is the max targets that the effect can hit
     */
    public WaveShoot(String name, String description, AmmoCubes cost, int nDamages, int nMarks, UltraDamage ultraDamage, int maxTargets) {
        super(name, description, cost, false, nDamages, nMarks, true, false, null, ultraDamage);
        this.maxTargets = maxTargets;
    }

    /**
     *
     * @return number of max targets that effect can hit.
     */
    public int getMaxTargets(){
        return maxTargets;
    }

    /**
     * Override of isValidEffect method from Effect class.
     * Checks if every target's position is 1 square far from shooter position
     * @param map is used to decide if the effect can be performed.
     * @return true if effect can be performed.
     */
    @Override
    public boolean isValidEffect(Map map) {
        if(getShooter() == null || getTargets() == null || getTargets().isEmpty() || map == null || getTargets().size() > maxTargets )
            return false;
        for(Player target: getTargets()) {
            int distance = map.getPlayersDistance(getShooter(), target);
            if(distance != 1) return false;
        }
        return true;
    }

    /**
     * Performs the effect
     * uses isValidEffect() if this method returns true performs the effect and deals damage to
     * targets
     * @param map is used to ''perform the effect''
     * @throws EffectException if effect is not valid.
     */
    @Override
    public void performEffect(Map map) throws EffectException {
        if(!isValidEffect(map)) throw new EffectException(NOT_VALID_EFFECT_MESSAGE);
        for(Player target: getTargets()) {
            target.sufferDamage(map, getShooter(), getnDamages(), getnMarks());
        }
        if(getUltraDamage() != null) {
            getUltraDamage().setShooter(getShooter());
            getUltraDamage().setTargets(getTargets());
        }
    }

    /**
     * Creates a list of possible targets that player can hit with this effect.
     * @param map is playing map
     * @param shooter is the player that is using th effect
     * @return a list of possible targets that player can hit.
     */
    @Override
    public List<Player> getPossibleTargets(Map map, Player shooter) {
        return map.getOtherPlayers(shooter, true).stream()
                .filter(target -> map.getPlayersDistance(shooter, target) == SHOOT_DISTANCE)
                .collect(Collectors.toList());
    }

    /**
     * Creates a list of possible square
     * @param map is map's
     * @param shooter is player that is using the effect
     * @return an empty collection because is a player based effect.
     */
    @Override
    public List<Square> getPossibleSquares(Map map, Player shooter) {
        return new ArrayList<>();
    }

    /**
     *
     * @return max number of enemies that player can select
     */
    @Override
    public int getNumTargetsToSelect() {
        return maxTargets;
    }

    /**
     *
     * @return max number of squares that player can select
     */
    @Override
    public int getNumSquaresToSelect() {
        return 0;
    }
}
