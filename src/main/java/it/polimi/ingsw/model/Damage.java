package it.polimi.ingsw.model;


/**
 * This class represents damage took by a player during game.
 * Inside we can find a Player that is the shooter and a boolean. If true damage is inferted.
 */
public class Damage {

    private Player shooter;
    private boolean infertDamage;

    /**
     * Construct that takes shooter Player.
     * @param shooter is the player shooter
     */
    public Damage(Player shooter) {
        this.shooter=shooter;
    }

    /**
     * Constructor
     * @param shooter is the player that has shot
     * @param infertDamage is true if is an infert damage.
     */
    public Damage(Player shooter,boolean infertDamage) {
        this.infertDamage=infertDamage;
        this.shooter = shooter;
    }

    /**
     *
     * @return the shooter
     */
    public Player getShooter() {
        return shooter;
    }

    /**
     *
     * @return true if player has infert damage.
     */
    public boolean isInfertDamage() { return infertDamage; }
}