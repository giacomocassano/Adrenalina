package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RangedShoot extends NormalShoot, it has just a min range and a max range in addiction.
 */
public class RangedShoot extends NormalShoot {


    private static final String NOT_VALID_EFFECT_MESSAGE = "L'effetto non è valido!";
    private static final String RECOIL_MOVEMENT_NOT_VALID_MESSAGE = "Movimento di rinculo non valido!";
    private int minRange;
    private int maxRange;

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
     * @param minRange is the minimum distance that enemies must be to be hit
     * @param maxRange is the max distance that enemies must be to be hit
     */
    public RangedShoot(String name, String description, AmmoCubes cost, int nDamages, int nMark, RecoilMovement recoil, UltraDamage ultraDamage, boolean shootVisible, int maxTarget, int minRange, int maxRange) {
        super(name, description, cost, nDamages, nMark, recoil, ultraDamage, shootVisible, maxTarget);
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    /**
     * Checks if effect is valid, so targets are between min range, max range, if there is no value that is null
     * @param map is playing map, used to check if effect is valid
     * @return true if effect is valid.
     */
    @Override
    public boolean isValidEffect(Map map) {
        if(this.getShooter() == null || this.getTargets() == null || map == null) return false;
        if(this.getTargets().isEmpty()  || this.getTargets().size() > super.getMaxTarget()) return false;
        for(Player target: this.getTargets())
            if(map.isVisible(this.getShooter(), target) != super.isShootVisible() || map.getPlayersDistance(this.getShooter(), target) < minRange || map.getPlayersDistance(this.getShooter(), target) > maxRange)
                return false;
        if(getRecoil() != null && !getRecoil().isValidEffect(map)) return false;
        return true;
    }

    /**
     * Performs the effect
     * @param map is the playing map
     * @throws EffectException if there is
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
     * @return max range of the effect
     */
    public int getMinRange() {
        return minRange;
    }

    /**
     *
     * @return min range of the effect
     */
    public int getMaxRange() {
        return maxRange;
    }

    /**
     *
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return a list of possible targets that shooter can hit with the effect
     */
    @Override
    public List<Player> getPossibleTargets(Map map, final Player shooter) {
        return super.getPossibleTargets(map, shooter).stream()
                        .filter(t -> map.getPlayersDistance(shooter, t) <= maxRange)
                        .filter(t -> map.getPlayersDistance(shooter, t) >= minRange)
                        .collect(Collectors.toList());
    }

}
