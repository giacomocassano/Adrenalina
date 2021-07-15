package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * NormalShoot is the basic shoot that a weapon can have.
 * It is used by basic weapons that have no strage usages.
 */
public class NormalShoot extends ShootEffect{

    private static final String NOT_VALID_EFFECT_MESSAGE = "L'effetto non è valido!";
    private static final String RECOIL_MOVEMENT_NOT_VALID_MESSAGE = "Movimento di rinculo non valido!";
    private boolean shootVisible;
    private int maxTarget;

    /**
     *
     * @param name is effect's name
     * @param description is effect's description
     * @param cost is effect's cost based on ammocubes
     * @param nDamages is max number of damage that effect deals
     * @param nMark is max number of marks that effect deals
     * @param recoil is the recoil effect related to this one
     * @param ultraDamage is the ultra effect related to this one
     * @param shootVisible is true if targets must be visible from shooter
     * @param maxTarget are max targets that effect can hit
     */
    public NormalShoot(String name, String description, AmmoCubes cost, int nDamages, int nMark, RecoilMovement recoil, UltraDamage ultraDamage, boolean shootVisible, int maxTarget) {
        super(name, description, cost, false, nDamages, nMark, true, false, recoil, ultraDamage);
        this.shootVisible = shootVisible;
        this.maxTarget = maxTarget;
    }

    /*@Override @TODO DA TOGLIERE?
    public boolean isValidEffect(Map map) {
        if(this.getShooter() == null || this.getTargets() == null || map == null) return false;
        if(this.getTargets().isEmpty()  || this.getTargets().size() > maxTarget) return false;
        for(Player target: this.getTargets())
            if(map.isVisible(this.getShooter(), target) != shootVisible) return false;
        if(getRecoil() != null && !getRecoil().isValidEffect(map)) return false;
        return true;
    }*/

    /**
     * Checks if effect is valid, so if there are no null players
     * @param map is the playing map. Is used by the method to see if effect can be performed.
     * @return true if effect is valid and ready to be performed.
     */
    @Override
    public boolean isValidEffect(Map map) {
        //Check nulls pointers
        if(this.getShooter() == null || this.getTargets() == null || map == null) return false;
        //Check targets
        if(!this.getPossibleTargets(map, getShooter()).containsAll(getTargets()) || getTargets().size() > maxTarget) return false;
        return true;
    }

    /**
     * Performs the effect adding damage to targets
     * @param map is the playing map and is used to perform the effect.
     * @throws EffectException if there is an error during the perform of the effect
     */
    @Override
    public void performEffect(Map map) throws EffectException {
        if(!isValidEffect(map)) throw new EffectException(NOT_VALID_EFFECT_MESSAGE);
        if(getRecoil() != null) {
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
     * @return if target can shoot only visible people
     */
    public boolean isShootVisible() {
        return shootVisible;
    }

    /**
     *
     * @return max number of targets to select
     */
    public int getMaxTarget() {
        return maxTarget;
    }

    /**
     *
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return a list of possible targets that shooter can hit with the effect
     */
    @Override
    public List<Player> getPossibleTargets(Map map, Player shooter) {
        return map.getOtherPlayers(shooter, shootVisible);
    }

    /**
     *
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return possible squares to select
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
        return maxTarget;
    }

    /**
     *
     * @return num of squares to select to perform the effect
     */
    @Override
    public int getNumSquaresToSelect() {
        return 0;
    }
}
