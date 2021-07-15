package it.polimi.ingsw.view;

import it.polimi.ingsw.events.model_to_view.*;
import it.polimi.ingsw.events.client_to_server.to_controller.EventFromView;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.EmpowerType;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.model.cards.effects.ShootEffect;
import it.polimi.ingsw.model.cards.effects.ShooterMovement;
import it.polimi.ingsw.model.cards.effects.UltraDamage;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utils.EventsFromViewReceiver;

import java.util.List;

/**
 * Class that represents the view on server side.
 * The view is splitted in two parts and this is the server side part.
 */
public class RemoteView extends View implements EventsFromViewReceiver {

    private ClientConnection clientConnection;

    /**
     * Constructor.
     * @param player is the player of the view.
     * @param clientConnection is the client connection object used to receive and send the events.
     */
    public RemoteView(Player player, ClientConnection clientConnection) {
        super(player);
        this.clientConnection = clientConnection;
        this.clientConnection.registerEventsReceiver(this);
    }

    /**
     * Method used to receive event from the client side view.
     * @param event is the received event.
     */
    @Override
    public void receiveEvent(EventFromView event) {
        this.getControllerUpdater().notify(event);
    }

    /**
     * Method used to update view with a generic element.
     * @param event is the generic update used to update view.
     */
    @Override
    public void update(EventFromModel event) {
        if(clientConnection.isActive())
            clientConnection.asyncSendEvent(event);
    }

    /**
     * Client connection setter.
     * @param clientConnection is the connection to set.
     */
    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        this.clientConnection.registerEventsReceiver(this);
    }

    /**
     * Method used by model to update the view with a spawn request.
     * @param activePlayer is the player to update
     */
    @Override
    public void notifySpawnRequest(Player activePlayer) {
        clientConnection.notifySpawnRequest(activePlayer);
    }

    /**
     * Method used by model to update the view with a move request.
     * @param activePlayer is the player to update
     * @param isFinalFrenzy is true if the game is in finaly frenzy
     * @param validMoves are the valids move that player can do
     * @param actionNumber is number of action
     * @param moveNumber is the number of the move
     */
    @Override
    public void notifyMoveRequest(Player activePlayer, boolean isFinalFrenzy, List<MoveType> validMoves, int actionNumber, int moveNumber) {
        clientConnection.notifyMoveRequest(activePlayer, isFinalFrenzy, validMoves, actionNumber, moveNumber);
    }


    /**
     * Method used by model to update the view with a square to move request.
     * @param activePlayer is the player to update
     * @param map is the game map
     */
    @Override
    public void notifySquareToMoveRequest(Player activePlayer, Map map) {
        clientConnection.notifySquareToMoveRequest(activePlayer, map);
    }

    /**
     * Method used by model to update the view with a grab ammo request.
     * @param activePlayer is the player to update
     * @param map is the game map
     */
    @Override
    public void notifyGrabAmmoRequest(Player activePlayer, Map map) {
        clientConnection.notifyGrabAmmoRequest(activePlayer, map);
    }

    /**
     * Method used by model to update the view with the end of the game.
     * @param player is the player to update
     * @param winner is the winner player
     * @param tiePlayers are the player that drew.
     * @param ranking is the final ranking.
     */
    @Override
    public void notifyEndGame(Player player, Player winner, List<Player> tiePlayers, java.util.Map<String, Integer> ranking) {
        clientConnection.notifyEndGame(player, winner, tiePlayers, ranking);
    }

    /**
     * Method used by model to update the view with a grab weapon request.
     * @param activePlayer is the player to update
     * @param map is the game map
     */
    @Override
    public void notifyGrabWeaponRequest(Player activePlayer, Map map) {
        clientConnection.notifyGrabWeaponRequest(activePlayer, map);
    }

    /**
     * Method used by model to update the view with a shoot weapon request
     * @param activePlayer is the player to update
     */
    @Override
    public void notifyShootWeaponRequest(Player activePlayer) {
        clientConnection.notifyShootWeaponRequest(activePlayer);
    }

    /**
     * Method used by model to update the view with a weapons to reload request.
     * @param activePlayer is the player to update
     */
    @Override
    public void notifyWeaponsToReload(Player activePlayer) {
        clientConnection.notifyWeaponsToReload(activePlayer);
    }

    /**
     * Method used by model to update the view with a information message
     * @param message is the information message
     */
    @Override
    public void notifyMessage(String message) {
        clientConnection.notifyMessage(message);
    }

    /**
     * Method used by model to update the view with a error message
     * @param errorMessage is the error message
     */
    @Override
    public void notifyErrorMessage(String errorMessage) {
        clientConnection.notifyErrorMessage(errorMessage);
    }

    /**
     * Method used by model to update the view with a empower request.
     * @param player is the active player.
     * @param valids are the valid empowers that player can uses.
     */
    @Override
    public void notifyEmpowerRequest(Player player, List<EmpowerType> valids) {
        clientConnection.notifyEmpowerRequest(player, valids);
    }


    /**
     * Method used by model to update the view with a empower use request.
     * @param activePlayer is the active player.
     * @param player is the player who uses empower.
     * @param map is the game map
     * @param players are the possible target
     * @param empower is the empower to use
     */
    @Override
    public void notifyEmpowerUseRequest(Player activePlayer, Player player, Map map, List<Player> players, EmpowerCard empower) {
        clientConnection.notifyEmpowerUseRequest(activePlayer, player, map, players, empower);
    }

    /**
     * Method used by model to update the view with a shooter movement request
     * @param player is the player that uses shooter movement
     * @param map is the game map
     * @param shooterMovement is the shooter movement effect
     */
    @Override
    public void notifyShooterMovementRequest(Player player, Map map, ShooterMovement shooterMovement){
        clientConnection.notifyShooterMovementRequest(player, map, shooterMovement);
    }

    /**
     * Method used by model to update the view with a basic effect request
     * @param activePlayer is the player that use basic effect
     * @param activeWeapon is the weapon that player shoots with
     */
    @Override
    public void notifyBasicEffectRequest(Player activePlayer, WeaponCard activeWeapon) {
        clientConnection.notifyBasicEffectRequest(activePlayer, activeWeapon);
    }

    /**
     * Method used by model to update the view with a respawn request.
     * @param dead is the dead player
     */
    @Override
    public void notifyRespawnRequest(Player dead) {
        clientConnection.notifyRespawnRequest(dead);
    }

    /**
     * Method used by model to update the view with a basic effect use request.
     * @param activePlayer is the active player
     * @param map is the game map
     * @param effect is the ultra effect
     */
    @Override
    public void notifyBasicEffectUseRequest(Player activePlayer, Map map, ShootEffect effect) {
        clientConnection.notifyBasicEffectUseRequest(activePlayer, map, effect);
    }

    /**
     * Method used by model to update the view with a ultra effect request.
     * @param activePlayer is the active player
     * @param ultra is the ultra effect
     */
    @Override
    public void notifyUltraEffectRequest(Player activePlayer, UltraDamage ultra) {
        clientConnection.notifyUltraEffectRequest(activePlayer, ultra);
    }

    /**
     * Method used by model to update the view with a empower use request.
     * @param player is the player that uses the empower
     * @param map is the game map
     * @param ultra is the ultra effect
     */
    @Override
    public void notifyUltraEffectUseRequest(Player player, Map map, UltraDamage ultra) {
        clientConnection.notifyUltraEffectUseRequest(player, map, ultra);
    }

    /**
     * Method used by model to signal when time exceeds.
     * @param message is a optional message
     */
    @Override
    public void notifyTimeExceeded(String message) {
        clientConnection.notifyTimeExceeded(message);
    }

    /**
     * Method used by model to notify the game representation
     * @param player is the player to update
     * @param game is model main class
     * @param gameStarting is true if game is starting
     */
    @Override
    public void notifyGameRepContext(Player player, Game game, boolean gameStarting) {
        clientConnection.notifyGameRepContext(player, game, gameStarting);
    }

}
