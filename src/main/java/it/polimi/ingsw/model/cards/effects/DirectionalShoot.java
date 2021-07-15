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
 * This is a weapon's effect.
 * It is a target based effect that hit only players in a certain direction.
 */
public class DirectionalShoot extends ShootEffect{


    private boolean decreased;
    private boolean cyber;
    private int maxRange;
    private int maxTargets;

    private static final String NOT_VALID_EFFECT_MESSAGE = "L'effetto non è valido!";
    private static final String RECOIL_MOVEMENT_NOT_VALID_MESSAGE = "Movimento di rinculo non valido!";

    /**
     *
     * @param name is effect's name
     * @param description is effect's description
     * @param cost is effect's cost based on ammocubes
     * @param nDamages is max number of damage that effect deals
     * @param nMarks is max number of marks that effect deals
     * @param recoil is the recoil effect related to this one
     * @param ultraDamage is the ultra effect related to this one
     * @param maxTargets are max targets that effect can hit
     * @param maxRange is the max distance that enemies must be to be hit
     * @param decreased is true if in the most distant squares/targets it does less damage
     * @param cyber is true if effect moves the target while doing damage
     */

    public DirectionalShoot(String name, String description, AmmoCubes cost, int nDamages, int nMarks, RecoilMovement recoil, UltraDamage ultraDamage, int maxTargets, int maxRange, boolean decreased, boolean cyber) {
        super(name, description, cost, false, nDamages, nMarks, true, false, recoil, ultraDamage);
        this.maxRange = maxRange;
        this.decreased = decreased;
        this.cyber = cyber;
        this.maxTargets = maxTargets;
    }

    /**
     * Checks if players are all on the same line, if there are no walls between them and if
     * there is no more than one target in the same square.
     * @param map is the playing map. Is used by the method to see if effect can be performed.
     * @return true if effect can be performed.
     */
    @Override
    public boolean isValidEffect(Map map) {
        if(getShooter() == null || getTargets() == null || getTargets().isEmpty() || map == null) return false;
        if(!map.checkTheLine(getShooter(), getTargets(), 1, maxRange)) return false;
        if(getTargets().size() > maxTargets) return false;
        for(Player t1: getTargets()) {
            if(!map.checkWalls(getShooter(), t1)) return false;
            for(Player t2: getTargets()) {
                if(t1 != t2)
                    if(map.getPlayerPosition(t1) == map.getPlayerPosition(t2)) return false;
            }
        }
        if(getRecoil() != null && !getRecoil().isValidEffect(map)) return false;
        return true;
    }

    /**
     * Performs the effect and deals damage to targets.
     * @param map is the playing map and is used to perform the effect.
     * @throws EffectException is isValidEffect() from the effect or
     * an effect related to him returns false.
     */
    @Override
    public void performEffect(Map map) throws EffectException {
        if(!isValidEffect(map)) throw new EffectException(NOT_VALID_EFFECT_MESSAGE);
        if(getRecoil() != null) {
            getRecoil().setPlayer(getTargets().get(0));
            if (!getRecoil().isValidEffect(map)) throw new EffectException(RECOIL_MOVEMENT_NOT_VALID_MESSAGE);
        }
        if (decreased){
            for(Player target: getTargets()) {
                int distance = map.getPlayersDistance(getShooter(), target);
                target.sufferDamage(map, getShooter(), getnDamages()+1-distance, 0);
            }
        }else{
            Player farther = getTargets().get(0);
            int disMax = map.getPlayersDistance(getShooter(), farther);
            for (Player target: getTargets()) {
                target.sufferDamage(map, getShooter(), getnDamages(), getnMarks() );
                int tmpDis = map.getPlayersDistance(getShooter(), target);
                if(tmpDis > disMax){
                    disMax = tmpDis;
                    farther = target;
                }
            }
            if(cyber) {
                map.getPlayerPosition(getShooter()).removePlayer(getShooter());
                map.getPlayerPosition(farther).addPlayer(getShooter());
            }
        }
        if(getUltraDamage() != null) {
            getUltraDamage().setShooter(getShooter());
            getUltraDamage().setTargets(getTargets());
        }

        if(getRecoil() != null) {
            getRecoil().performEffect(map);
        }
    }

    /**
     * @return decreased flag.
     */
    public boolean isDecreased() {
        return decreased;
    }

    /**
     * @return Cyber flag.
     */
    public boolean isCyber() {
        return cyber;
    }

    /**
     * @return MaxRange of the effect.
     */
    public int getMaxRange() {
        return maxRange;
    }

    /**
     * @return MaxTargets of the effect;
     */
    public int getMaxTargets(){ return maxTargets;}

    /**
     * Creates a list of possible targets that player can hit
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return a list of shootable players.
     */
    @Override
    public List<Player> getPossibleTargets(Map map, Player shooter) {
        List<Player> targets = map.getPlayersOnMap();
        targets = targets.stream()
                .filter(t -> map.checkWalls(shooter, t))
                .filter(t -> t != shooter)
                .filter(t -> map.getPlayersDistance(shooter, t) > 0)
                .filter(t -> map.getPlayersDistance(shooter, t) <= maxRange)
                .collect(Collectors.toList());
        return targets;
    }

    /**
     * return a list of possible squares that player can hit.
     * @param map is the playing map
     * @param shooter is the player that is shooting(using the effect)
     * @return a list of possible shootable squares
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
     * @return number of squares to select
     */
    @Override
    public int getNumSquaresToSelect() {
        return 0;
    }
}
