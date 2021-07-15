package it.polimi.ingsw.utils;

import it.polimi.ingsw.events.model_to_view.EventFromModel;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.MoveType;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.EmpowerType;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.model.cards.effects.ShootEffect;
import it.polimi.ingsw.model.cards.effects.ShooterMovement;
import it.polimi.ingsw.model.cards.effects.UltraDamage;

import java.util.List;

/**
 * Interface that will be implemented by View.
 * This interface represents the Observer Pattern logic.
 */
public interface ModelObserver {
    /**
     * Player getter.
     * @return the correspondent player of the view.
     */
    Player getPlayer();

    /**
     * Method used to update view with a generic element.
     * @param event is the generic update used to update view.
     */
    void update(EventFromModel event);

    /**
     * Method used by model to update the view with a spawn request.
     * @param activePlayer is the player to update
     */
    void notifySpawnRequest(Player activePlayer);

    /**
     * Method used by model to update the view with a move request.
     * @param activePlayer is the player to update
     * @param isFinalFrenzy is true if the game is in finaly frenzy
     * @param validMoves are the valids move that player can do
     * @param actionNumber is number of action
     * @param moveNumber is the number of the move
     */
    void notifyMoveRequest(Player activePlayer, boolean isFinalFrenzy, List<MoveType> validMoves, int actionNumber, int moveNumber);

    /**
     * Method used by model to update the view with a square to move request.
     * @param activePlayer is the player to update
     * @param map is the game map
     */
    void notifySquareToMoveRequest(Player activePlayer, Map map);

    /**
     * Method used by model to update the view with a grab ammo request.
     * @param activePlayer is the player to update
     * @param map is the game map
     */
    void notifyGrabAmmoRequest(Player activePlayer, Map map);

    /**
     * Method used by model to update the view with the end of the game.
     * @param player is the player to update
     * @param winner is the winner player
     * @param tiePlayers are the player that drew.
     * @param ranking is the final ranking.
     */
    void notifyEndGame(Player player, Player winner, List<Player> tiePlayers, java.util.Map<String, Integer> ranking);

    /**
     * Method used by model to update the view with a grab weapon request.
     * @param activePlayer is the player to update
     * @param map is the game map
     */
    void notifyGrabWeaponRequest(Player activePlayer, Map map);

    /**
     * Method used by model to update the view with a shoot weapon request
     * @param activePlayer is the player to update
     */
    void notifyShootWeaponRequest(Player activePlayer);

    /**
     * Method used by model to update the view with a weapons to reload request.
     * @param activePlayer is the player to update
     */
    void notifyWeaponsToReload(Player activePlayer);

    /**
     * Method used by model to update the view with a information message
     * @param message is the information message
     */
    void notifyMessage(String message);

    /**
     * Method used by model to update the view with a error message
     * @param errorMessage is the error message
     */
    void notifyErrorMessage(String errorMessage);

    /**
     * Method used by model to update the view with a respawn request.
     * @param dead is the dead player
     */
    void notifyRespawnRequest(Player dead);

    /**
     * Method used by model to signal when time exceeds.
     * @param message is a optional message
     */
    void notifyTimeExceeded(String message);

    /**
     * Method used by model to update the view with a empower request.
     * @param player is the active player.
     * @param valids are the valid empowers that player can uses.
     */
    void notifyEmpowerRequest(Player player, List<EmpowerType> valids);

    /**
     * Method used by model to update the view with a empower use request.
     * @param activePlayer is the active player.
     * @param player is the player who uses empower.
     * @param map is the game map
     * @param players are the possible target
     * @param empower is the empower to use
     */
    void notifyEmpowerUseRequest(Player activePlayer, Player player, Map map, List<Player> players, EmpowerCard empower);

    /**
     * Method used by model to update the view with a empower use request.
     * @param player is the player that uses the empower
     * @param map is the game map
     * @param ultra is the ultra effect
     */
    void notifyUltraEffectUseRequest(Player player, Map map, UltraDamage ultra);

    /**
     * Method used by model to update the view with a shooter movement request
     * @param player is the player that uses shooter movement
     * @param map is the game map
     * @param shooterMovement is the shooter movement effect
     */
    void notifyShooterMovementRequest(Player player, Map map, ShooterMovement shooterMovement);

    /**
     * Method used by model to update the view with a basic effect request
     * @param activePlayer is the player that use basic effect
     * @param activeWeapon is the weapon that player shoots with
     */
    void notifyBasicEffectRequest(Player activePlayer, WeaponCard activeWeapon);

    /**
     * Method used by model to update the view with a basic effect use request.
     * @param activePlayer is the active player
     * @param map is the game map
     * @param effect is the ultra effect
     */
    void notifyBasicEffectUseRequest(Player activePlayer, Map map, ShootEffect effect);

    /**
     * Method used by model to update the view with a ultra effect request.
     * @param activePlayer is the active player
     * @param ultra is the ultra effect
     */
    void notifyUltraEffectRequest(Player activePlayer, UltraDamage ultra);

    /**
     * Method used by model to notify the game representation
     * @param player is the player to update
     * @param game is model main class
     * @param gameStarting is true if game is starting
     */
    void notifyGameRepContext(Player player, Game game, boolean gameStarting);

}

