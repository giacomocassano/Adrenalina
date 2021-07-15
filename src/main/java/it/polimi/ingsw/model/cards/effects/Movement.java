package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

/**
 * This class represents a Move Effect.
 * A move effect is an effect that allows a movement that can be a shooter movement, so
 * the player that is using the effect can move after/before/during the effect, or a
 * recoil movement, so target can be recoiled in a certain position
 */
public abstract class Movement extends Effect {
    private int maxSteps;
    private Player player;
    private Square position;

    /**
     * Costructor
     * @param name is effect's name
     * @param description is effect's description
     * @param cost is the cost expressed in cubes
     * @param maxSteps is the max number of steps
     */
    public Movement(String name, String description, AmmoCubes cost, int maxSteps) {
        super(name, description, cost, 0, 0);
        this.maxSteps = maxSteps;
    }

    /**
     * sets player that has to move
     * @param player is player that has to move
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * sets position
     * @param position is the position where player has to move
     */
    public void setPosition(Square position) {
        this.position = position;
    }

    /**
     *
     * @return max steps that player can move
     */
    public int getMaxSteps() {
        return maxSteps;
    }

    /**
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @return position
     */
    public Square getPosition() {
        return position;
    }
}
