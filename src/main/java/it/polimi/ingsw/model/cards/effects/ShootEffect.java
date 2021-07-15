package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *Shoot effect is an abstract class that is extended by all the effects that involve the action of shooting.
 *  Each effect has one of some targets, a shooter,can have an ultra damage or a recoil movement, can be area based,
 *  target based or square based.
 */
public abstract class ShootEffect extends Effect{

    private Player shooter;
    private List<Player> targets;
    private List<Square> squares;
    private RecoilMovement recoil;
    private UltraDamage ultraDamage;
    private boolean area;
    private boolean isTargetBased, isSquareBased;

    /**
     * Constructor
     * @param name is the name of the effect
     * @param description is the description of the effect
     * @param cost is the cost of effect expressed in cubes
     * @param area is true if effect is area based
     * @param nDamages is max number of damage that effect deals
     * @param nMarks is max number of marks that effect deals
     * @param isTargetBased is true if effect is target based
     * @param isSquareBased is true if effect is square based
     * @param recoil is the recoil effect related to this one
     * @param ultraDamage is the ultra damage effect related to this one
     */
    public ShootEffect(String name, String description, AmmoCubes cost, boolean area, int nDamages, int nMarks, boolean isTargetBased, boolean isSquareBased, RecoilMovement recoil, UltraDamage ultraDamage) {
        super(name, description, cost, nDamages, nMarks);
        this.area = area;
        this.recoil = recoil;
        this.ultraDamage = ultraDamage;
        this.targets = new ArrayList<>();
        this.squares = new ArrayList<>();
        this.isSquareBased = isSquareBased;
        this.isTargetBased = isTargetBased;
    }

    /**
     *
     * @return the shooter
     */
    public Player getShooter() {
        return shooter;
    }

    /**
     * shooter setter
     * @param shooter is the shooter
     */
    public void setShooter(Player shooter) {
        this.shooter = shooter;
    }

    /**
     *
     * @return the targets
     */
    public List<Player> getTargets() {
        return targets;
    }

    /**
     * targets setter
     * @param targets are the targets
     */
    public void setTargets(List<Player> targets) {
        this.targets = targets;
    }

    /**
     *
     * @return squares where effect can be performed
     */
    public List<Square> getSquares() {
        return squares;
    }

    /**
     *
     * @param squares are the squares where effect can be performed
     */
    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    /**
     *
     * @return effect's recoil effect
     */
    public RecoilMovement getRecoil() {
        return recoil;
    }

    /**
     *
     * @return effect's ultra damage effect
     */
    public UltraDamage getUltraDamage() {
        return ultraDamage;
    }

    /**
     *
     * @return true if effect is an area one
     */
    public boolean isArea() {
        return area;
    }

    /**
     * adds a new target to list of targets
     * @param target is the new targe
     */
    public void addTarget(Player target) {
        targets.add(target);
    }

    /**
     * resets all the targets of the effect
     */
    public void resetTargets() {
        this.targets = new ArrayList<>();
    }

    /**
     *
     * @param square is a square
     */
    public void addSquare(Square square) {
        squares.add(square);
    }

    /**
     *
     * @param targetBased is true if effect is targets based
     */
    public void setTargetBased(boolean targetBased) {
        isTargetBased = targetBased;
    }

    /**
     *
     * @param squareBased is true if effect is square based
     */
    public void setSquareBased(boolean squareBased) {
        isSquareBased = squareBased;
    }

    /**
     *
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return a list of possible targets
     */
    public abstract List<Player> getPossibleTargets(Map map, Player shooter);

    /**
     *
     * @param map is the playing map
     * @param shooter is the player that is using the effect
     * @return a list of possible squares that shooter can hit.
     */
    public abstract List<Square> getPossibleSquares(Map map, Player shooter);

    /**
     *
     * @return number of target to select
     */
    public abstract int getNumTargetsToSelect();

    /**
     *
     * @return number of squares to select
     */
    public abstract int getNumSquaresToSelect();
}
