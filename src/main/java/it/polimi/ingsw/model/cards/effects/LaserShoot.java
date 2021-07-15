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
 * Lasershot is a weapon effect that allows shooter to hit enemies that
 * are behind a wall or other structures.
 */
public class LaserShoot extends ShootEffect {

    private static final String NOT_VALID_EFFECT_MESSAGE = "L'effetto non è valido!";
    private static final String RECOIL_MOVEMENT_NOT_VALID_MESSAGE = "Movimento di rinculo non valido!";
    private int maxTargets;


    /** @param name is effect's name
     * @param description is effect's description
     * @param cost is effect's cost based on ammocubes
     * @param nDamages is max number of damage that effect deals
     * @param nMarks is max number of marks that effect deals
     * @param recoil is the recoil effect related to this one
     * @param ultraDamage is the ultra effect related to this one
     * @param maxTargets are max targets that effect can hit
     */
    public LaserShoot(String name, String description, AmmoCubes cost, int nDamages, int nMarks, RecoilMovement recoil, UltraDamage ultraDamage, int maxTargets) {
        super(name, description, cost, false, nDamages, nMarks, true, false, recoil, ultraDamage);
        this.maxTargets = maxTargets;
    }

    /**
     * Checks if effect is valid
     * Checks if there are walls between shooter and targets and other controls
     * @param map is the playing map. Is used by the method to see if effect can be performed.
     * @return true if effect is valid
     */
    @Override
    public boolean isValidEffect(Map map) {
        if(getShooter() == null || getTargets() == null || getTargets().isEmpty() || getTargets().contains(getShooter()) || map == null) return false;
        if(!map.checkTheLine(getShooter(), getTargets(), 0, 5)) return false;
        if((getTargets().size() > getMaxTargets()) ||(getRecoil() != null && !getRecoil().isValidEffect(map))) return false;
        return true;
    }

    /**
     * Performs the effect and gives to targets damage.
     * @param map is the playing map and is used to perform the effect.
     * @throws EffectException if there is an error during effect perform or the perform of another
     * effect related to this like recoil effect or ultra damage effect.
     */
    @Override
    public void performEffect(Map map) throws EffectException {
        if (!isValidEffect(map)) throw new EffectException(NOT_VALID_EFFECT_MESSAGE);
        if (getRecoil() != null) {
            getRecoil().setPlayer(getTargets().get(0));
            if (!getRecoil().isValidEffect(map)) throw new EffectException(RECOIL_MOVEMENT_NOT_VALID_MESSAGE);
        }
        for(Player target: this.getTargets())
            target.sufferDamage(map, this.getShooter(), this.getnDamages(), this.getnMarks());
        if(getUltraDamage() != null) {
            getUltraDamage().setShooter(getShooter());
            getUltraDamage().setTargets(getTargets());
        }
        if(getRecoil() != null){
            getRecoil().performEffect(map);
        }
    }

    /**
     *
     * @return num of max targets
     */
    public int getMaxTargets() {
        return maxTargets;
    }

    /**
     * Creates a list of possible targets that effect can hit
     * checks for player in the same column and row of the shooter
     * @param map is the playing map, used to perform the effect
     * @param shooter is the shooter that is using the effect
     * @return a list of shootable players
     */
    @Override
    public List<Player> getPossibleTargets(Map map, Player shooter) {
        Square shPos = map.getPlayerPosition(shooter);
        return map.getPlayersOnMap().stream()
                .filter(t -> t != shooter)
                .filter(t -> map.getPlayerPosition(t).getRow() == shPos.getRow() || map.getPlayerPosition(t).getColumn() == shPos.getColumn())
                .collect(Collectors.toList());
    }

    /**
     * In this case return an empty collection because effect is target based
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return a list of possible square that shooter can hit.
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
        return maxTargets;
    }

    /**
     *
     * @return number of squares to select, 0 for this effect.
     */
    @Override
    public int getNumSquaresToSelect() {
        return 0;
    }
}
