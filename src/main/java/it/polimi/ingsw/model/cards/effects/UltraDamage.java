package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ultra damage effect is the ultra effect that some weapons have.
 * Is a simple effect that can be used during base effect or later,
 * can hits old targets or not..
 */
public class UltraDamage extends ShootEffect {


    private static final String OPTIONAL_EFFECT_NOT_VALID_MESSAGE = "L'effetto opzionale non è valido!";
    private int maxTargets;
    private boolean chain;
    private boolean oldTargetsShootable;
    private boolean vortex;
    private List<Player> newTargets;
    private boolean duringBasicEffect;
    private Square damageSquare;


    /**
     * Constructor
     * @param name is effect's name
     * @param description is effect's description
     * @param cost is effect's cost expressed in cubes
     * @param nDamages is number of marks that effect deals
     * @param nMarks is number of damages that effect deals
     * @param ultraDamage is the related ultra damage effect
     * @param maxTargets is the max number of targets that this effect can hit
     * @param chain is a flag that express if effect must be chained to the first one.
     * @param oldTargetsShootable is a flag that is true if old targets are shootable
     * @param area is true if effect is an area effect
     * @param vortex is true if effect creates a vortex that moves players
     * @param duringBasicEffect is true if effect can be casted during basic effect
     */
    public UltraDamage(String name, String description, AmmoCubes cost, int nDamages, int nMarks, UltraDamage ultraDamage, int maxTargets, boolean chain, boolean oldTargetsShootable, boolean area, boolean vortex, boolean duringBasicEffect) {
        super(name, description, cost, area, nDamages, nMarks, true, true, null, ultraDamage);
        if(chain || oldTargetsShootable || vortex) {
            super.setTargetBased(true);
            super.setSquareBased(false);
        }else if(area) {
            super.setTargetBased(false);
            super.setSquareBased(true);
        }
        this.maxTargets = maxTargets;
        this.chain = chain;
        this.oldTargetsShootable = oldTargetsShootable;
        this.vortex = vortex;
        this.duringBasicEffect=duringBasicEffect;
        newTargets = new ArrayList<>();
    }

    /**
     * Checks if effect is valid.
     * Checks things like if enemies selected are more than selectable targets, if shooter selects an enemy that can't be shot anymore
     * @param map is the playing map. Is used by the method to see if effect can be performed.
     * @return true if effect can be performed.
     */
    @Override
    public boolean isValidEffect(Map map) {
        if((!isArea() && (newTargets == null || newTargets.isEmpty())) || (isArea() && (damageSquare == null)) || getNewTargets().size() > maxTargets) return false;
        if(chain) {
            Player oldTarget = getTargets().get(0);
            if(!map.getOtherPlayers(oldTarget, true).containsAll(newTargets) || newTargets.size()!=1 || getTargets().size()!= 1)
                return false;
        }else if(vortex){
            Square vortexSquare = getSquares().get(0);
            for(Player target: getNewTargets()){
                Square targetPos = map.getPlayerPosition(target);
                int distance = map.getSquareDistance(targetPos, vortexSquare);
                if(distance > 1) return false;
            }
        }else if(oldTargetsShootable){
            if(newTargets.size() > maxTargets || !getTargets().containsAll(newTargets)) return false;
        }else if(this.isArea()) {
            if(damageSquare == null  || damageSquare.getPlayers().contains(getShooter()) || !map.getOtherSquare(map.getPlayerPosition(getShooter()), true).contains(damageSquare))
                return false;
        }
        return true;
    }

    /**
     *
     * @return if the ultra effect can be performed during basic effect.
     */
    public boolean isDuringBasicEffect() {
        return duringBasicEffect;
    }


    /**
     * Performs the effect if effect is valid.
     * @param map is the playing map and is used to perform the effect.
     * @throws EffectException if effect can't be performed, like there is a null value.
     */
    @Override
    public void performEffect(Map map) throws EffectException {
        if(!isValidEffect(map)) throw new EffectException(OPTIONAL_EFFECT_NOT_VALID_MESSAGE);
        if(this.isArea() && damageSquare != null) {
            List<Player> targets = damageSquare.getPlayers();
            targets.remove(getShooter());
            newTargets.addAll(targets);
            for(Player target: targets)
                if(target != getShooter())
                    target.sufferDamage(map, getShooter(), getnDamages(), getnMarks());
            if(getUltraDamage() != null) {
                getUltraDamage().setShooter(getShooter());
                getUltraDamage().setTargets(targets);
            }
        }else{
            for(Player target: getNewTargets()) {
                if(target != getShooter())
                    target.sufferDamage(map, getShooter(), getnDamages(), getnMarks());
                if(vortex){
                    Square vortexSquare = map.getPlayerPosition(getTargets().get(0));
                    map.movePlayer(target, vortexSquare);
                }
            }
            if(getUltraDamage() != null) {
                getUltraDamage().setShooter(getShooter());
                getUltraDamage().setTargets(getNewTargets());
            }
        }
    }

    /**
     *
     * @param newTargets are new targets of the effect
     */
    public void setNewTargets(List<Player> newTargets) {
        this.newTargets = newTargets;
    }

    /**
     *
     * @return num of max targets
     */
    public int getMaxTargets() {
        return maxTargets;
    }

    /**
     *
     * @return if effect is chainable
     */
    public boolean isChain() {
        return chain;
    }

    /**
     *
     * @return if effect is a vortex one.
     */
    public boolean isVortex() {
        return vortex;
    }

    /**
     *
     * @return true if old targets are shootable
     */
    public boolean areOldTargetsShootable() {
        return oldTargetsShootable;
    }

    /**
     *
     * @return new effect targets.
     */
    public List<Player> getNewTargets() {
        return newTargets;
    }

    /**
     * add a new target to list of targets
     * @param target are new targets
     */
    public void addNewTarget(Player target) {
        this.newTargets.add(target);
    }

    /**
     * resets the list of new targets
     */
    public void resetNewTarget() {
        this.newTargets = new ArrayList<>();
    }

    /**
     * Creates a list of possible enemies that are possible targets.
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return a list of possible targets that shooter can hit
     */
    @Override
    public List<Player> getPossibleTargets(Map map, Player shooter) {
        if(chain) {
            Player oldTarget = getTargets().get(0);
            List<Player> visiblesByOldTarget = map.getOtherPlayers(oldTarget, true);
            visiblesByOldTarget.remove(shooter);
            return visiblesByOldTarget;
        }
        if(vortex) {
            Square vortexSquare = getSquares().get(0);
            Player oldTarget = getTargets().get(0);
            return map.getPlayersOnMap().stream().filter(p -> (p != shooter && p != oldTarget))
                                .filter(p -> map.getSquareDistance(map.getPlayerPosition(p), vortexSquare) <= 1)
                                .collect(Collectors.toList());
        }
        if(oldTargetsShootable) {
            return getTargets();
        }
        if(isArea())
            return new ArrayList<>();
        return  map.getPlayersOnMap().stream()
                .filter(p -> p != shooter)
                .filter(p -> !getTargets().contains(p))
                .collect(Collectors.toList());

    }

    /**
     * damage square flag getter
     * @return damage square flag
     */
    public Square getDamageSquare() {
        return damageSquare;
    }

    /**
     * damage square setter
     * @param damageSquare is a flag
     */
    public void setDamageSquare(Square damageSquare) {
        this.damageSquare = damageSquare;
    }

    /**
     * Gets a list of possible squares that shooter can select to hit.
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return a list of shootable squares.
     */
    @Override
    public List<Square> getPossibleSquares(Map map, Player shooter) {
        //If is area damages, the possible squares are the squares visible by the shooter
        if(isArea()) {
            List<Square> possibleSquares = map.getOtherSquare(map.getPlayerPosition(shooter), true);
            possibleSquares.remove(map.getPlayerPosition(shooter));
            return possibleSquares;
        }
        return new ArrayList<>();
    }

    /**
     *
     * @return num of targets to select
     */
    @Override
    public int getNumTargetsToSelect() {
        return maxTargets;
    }

    /**
     *
     * @return num of squares to select
     */
    @Override
    public int getNumSquaresToSelect() {
        if(isArea())
            return 1;
        return 0;
    }
}
