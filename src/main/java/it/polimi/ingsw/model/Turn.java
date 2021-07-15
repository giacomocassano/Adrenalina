package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.model.cards.effects.ShootEffect;
import it.polimi.ingsw.model.cards.effects.UltraDamage;

import java.util.List;

/**
 * This class represent a Game turn. A game turn is composed by two action in normal rules.
 * A turn has an active player, a list of actions, expected two.
 */
public class Turn {

    private Player activePlayer;
    private int remainingActions;
    private Action activeAction;
    private WeaponCard activeWeapon;
    private ShootEffect activeEffect;
    private UltraDamage activeUltra;
    private List<Player> activeEmpowerPlayers;
    private boolean isEnded;

    /**
     * @return active player.
     */
    public Player getActivePlayer() {
        return activePlayer;
    }

    /**
     * Removes
     * @param player from
     */
    public void removeEmpowerPlayer(Player player) {
        this.activeEmpowerPlayers.remove(player);
    }

    /**
     *clears the empower's player
     */
    public void clearEmpowerPlayers() {
        this.activeEmpowerPlayers.clear();
    }

    /**
     *
     * @param activePlayer is player active in the turn
     */
    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     *
     * @return remaining actions
     */
    public int getRemainingActions() {
        return remainingActions;
    }

    /**
     *
     * @param remainingActions are player's remaining action in the turn.
     */
    public void setRemainingActions(int remainingActions) {
        this.remainingActions = remainingActions;
    }

    /**
     * @return active action
     */
    public Action getActiveAction() {
        return activeAction;
    }

    /**
     *sets the active action
     * @param activeAction is the active action
     */
    public void setActiveAction(Action activeAction) {
        this.activeAction = activeAction;
    }

    /**
     * @return the active weapon
     */
    public WeaponCard getActiveWeapon() {
        return activeWeapon;
    }

    /**
     * sets the Active Weapon
     * @param activeWeapon is the active weapon.
     */
    public void setActiveWeapon(WeaponCard activeWeapon) {
        this.activeWeapon = activeWeapon;
    }

    /**
     * @return the active effect.
     */
    public ShootEffect getActiveEffect() {
        return activeEffect;
    }

    /**
     *set active effect
     * @param activeEffect is active effect
     */
    public void setActiveEffect(ShootEffect activeEffect) {
        this.activeEffect = activeEffect;
    }


    /**
     * active empower player's getter
     * @return active empower players
     */
    public List<Player> getActiveEmpowerPlayers() {
        return activeEmpowerPlayers;
    }

    /**
     * active empower player's setter
     * @param activeEmpowerPlayers are players with an active empower
     */
    public void setActiveEmpowerPlayers(List<Player> activeEmpowerPlayers) {
        this.activeEmpowerPlayers = activeEmpowerPlayers;
    }

    /**
     *
     * @return the ultra effect that is active
     */
    public UltraDamage getActiveUltra() {
        return activeUltra;
    }

    /**
     * Sets the active ultra effect
     * @param activeUltra is the active effect
     */
    public void setActiveUltra(UltraDamage activeUltra) {
        this.activeUltra = activeUltra;
    }

    /**
     * checks if turn has ended
     * @return true if turn has ended
     */
    public boolean isEnded() {
        return isEnded;
    }

    /**
     * Ends turn
     */
    public void endTurn() {
        isEnded = true;
    }

    /**
     * Starts the turn.
     */
    public void startTurn() {
        isEnded = false;
        activeWeapon = null;
        activeEffect = null;
        activeUltra = null;
    }
}
