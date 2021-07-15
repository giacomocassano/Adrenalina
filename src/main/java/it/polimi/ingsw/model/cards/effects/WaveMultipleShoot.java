package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * WaveMultipleShoot is a weapon effect based on squares.
 * Hits everyone at distance 1 from shooter position
 */
public class WaveMultipleShoot extends ShootEffect{

    private static final String NOT_VALID_EFFECT_MESSAGE = "L'effetto non è valido!";

    /**
     * Constructor
     * @param name is effect's name
     * @param description is effect's description
     * @param cost is effect's cost based in ammocubes
     * @param nDamages is max number of damages that effect deals
     * @param nMarks is max number of marks that effect deals
     * @param ultraDamage is the ultra damage effect related to this effect
     */
    public WaveMultipleShoot(String name, String description, AmmoCubes cost, int nDamages, int nMarks, UltraDamage ultraDamage) {
        super(name, description, cost, true, nDamages, nMarks, false, false, null, ultraDamage);
    }

    /**
     * Checks if players are one square far from shooter and if there are no null values
     * and if there is at least one target in near squares
     * @param map is the playing map. Is used by the method to see if effect can be performed.
     * @return true if effect can be performed
     */
    @Override
    public boolean isValidEffect(Map map) {
        if(getShooter() == null || map == null)
            return false;
        List<Square> nearSquares = map.getNearSquares(map.getPlayerPosition(getShooter()));
        boolean atLeastOneTarget = false;
        for(Square square: nearSquares)
            if(!square.getPlayers().isEmpty())
                atLeastOneTarget = true;
        if(!atLeastOneTarget) return false;
        return true;
    }

    /**
     * Performs the effect
     * @param map is the playing map and is used to perform the effect.
     * @throws EffectException if this effect or another effect related, like
     * recoil effect or ultra damage effect aren't valid.
     */
    @Override
    public void performEffect(Map map) throws EffectException {
        if(!isValidEffect(map)) throw new EffectException(NOT_VALID_EFFECT_MESSAGE);
        List<Square> nearSquares = map.getNearSquares(map.getPlayerPosition(getShooter()));
        for(Square square: nearSquares) {
            getTargets().addAll(square.getPlayers());
            for(Player target: square.getPlayers())
                target.sufferDamage(map, getShooter(), getnDamages(), getnMarks());
        }
        if(getUltraDamage() != null) {
            getUltraDamage().setShooter(getShooter());
            getUltraDamage().setTargets(getTargets());
        }
    }

    /**
     *
     * @param map is playing map
     * @param shooter is the player that is using the effect
     * @return a list of possible targets, an empty list in this case.
     */
    @Override
    public List<Player> getPossibleTargets(Map map, Player shooter) {
        return new ArrayList<>();
    }

    /**
     *
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return a list of possible squares that shooter can hit with the effect
     */
    @Override
    public List<Square> getPossibleSquares(Map map, Player shooter) {
        return new ArrayList<>();
    }

    /**
     *
     * @return num of targets to select
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
        return 0;
    }
}
