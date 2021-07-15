package it.polimi.ingsw.client;

import it.polimi.ingsw.client.client_thread.ClientPingThread;
import it.polimi.ingsw.client.user_interfaces.UserInterface;
import it.polimi.ingsw.client.user_interfaces.model_representation.EffectInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.events.client_to_server.ServerEvent;
import it.polimi.ingsw.events.client_to_server.to_controller.*;
import it.polimi.ingsw.events.client_to_server.to_server.PingServer;
import it.polimi.ingsw.model.MoveType;
import it.polimi.ingsw.model.Position;

import java.util.List;

/**
 * Abstract class that will be extended by RMIServerConnection and SocketServerConnection.
 * It represents the connection with the server.
 */
public abstract class ServerConnection {
    private final UserInterface userInterface;
    private boolean isActive;
    private ClientPingThread clientPingThread;
    private final Client client;

    /**
     * Constructor.
     * @param client is the generic Client. It can be RMI or Socket Client
     * @param userInterface is the chosen user interface. It can be GUI or CLI
     */
    public ServerConnection(Client client, UserInterface userInterface) {
        this.client = client;
        this.userInterface = userInterface;
        userInterface.setServerConnection(this);
        this.isActive = true;
    }

    /**
     * Signals to the client ping thread that the connection with client is still active.
     */
    public void pingReceived() {
        if(this.clientPingThread != null)
            this.clientPingThread.setConnected(true);
    }

    /**
     * Start the ping thread. It controls if the connection with client is active.
     */
    public void startPingThread() {
        this.clientPingThread = new ClientPingThread(this, userInterface);
        new Thread(this.clientPingThread).start();
    }

    /**
     * active Setter
     * @param active true if connection is active
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * UserInterface Getter
     * @return the user interface
     */
    public UserInterface getUserInterface() {
        return userInterface;
    }

    /**
     * Active Getter
     * @return true is the connection is active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Client ping thread getter.
     * @return the client ping thread.
     */
    public ClientPingThread getClientPingThread() {
        return clientPingThread;
    }

    /**
     * Client Getter
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Sends an event to the client in a new thread
     * @param event is ethe event to send
     */
    public abstract void asyncSendEvent(ServerEvent event);

    /* Notify methods */

    /**
     * Notifies the move response
     * @param player is the player that perform a move
     * @param moveType is the type of the chosen move
     */
    public void notifyMoveResponse(String player, MoveType moveType) {
        MoveResponse response = new MoveResponse(player, moveType);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Notifies the first spawn response
     * @param player is the spawn player
     * @param empToDrop is the empower to drop
     * @param empowers are the other empowers of the player
     */
    public void notifyFirstSpawnResponse(String player, EmpowerInfo empToDrop, List<EmpowerInfo> empowers) {
        FirstSpawnResponse event = new FirstSpawnResponse();
        event.setPlayer(player);
        event.setDroppedEmpower(empToDrop);
        event.setOtherEmpower(empowers);
        new Thread(() -> asyncSendEvent(event)).start();
    }

    /**
     * Notifies the chosen square where the player wants to move
     * @param playerName is the player who wants to move
     * @param position is the chosen position
     * @param aborted is true if the player wants to abort the action
     */
    public void notifySquareToMoveResponse(String playerName, Position position, boolean aborted) {
        SquareToMoveResponse response = new SquareToMoveResponse(playerName, position, aborted);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Notifies the chosen empower
     * @param playerName is the player who wants to use the empower
     * @param empowerInfo is the chosen empower
     * @param wantUse is tue if the player wants to use the empower
     */
    public void notifyEmpowerResponse(String playerName, EmpowerInfo empowerInfo, boolean wantUse) {
        EmpowerResponse response = new EmpowerResponse(playerName, empowerInfo, wantUse);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Notifies the weapon the user want to shoot with
     * @param player is the player who wants to shoot
     * @param weapon is the choosen weapon
     * @param isAborted is true if the player wants to abort the action
     */
    public void notifyWeaponToShootResponse(String player, WeaponInfo weapon, boolean isAborted){
        WeaponToShootResponse response = new WeaponToShootResponse(player, weapon, isAborted);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Notifies the chosen basic effect
     * @param player is the player who wants to use a basic effect
     * @param aborted is true if the player wants to abort the action
     * @param effect is the chosen basic effect
     * @param payingEmps are the empowers that the player wants to pay with
     */
    public void notifyBasicEffectResponse(String player, boolean aborted, EffectInfo effect, List<EmpowerInfo> payingEmps) {
        BasicEffectResponse response = new BasicEffectResponse(player, false, effect, payingEmps);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Notifies the the weapon that the user wants to grab
     * @param player is the player who wants to grab
     * @param gainedWeapon is the grabber weapon
     * @param droppedWeapon is the dropped weapon if the player has 3 weapons
     * @param payingEmps are the empowers that the player wants to pay with
     * @param aborted is true if the player wants to abort the action
     */
    public void notifyWeaponToGrabResponse(String player, WeaponInfo gainedWeapon, WeaponInfo droppedWeapon, List<EmpowerInfo> payingEmps, boolean aborted) {
        WeaponToGrabResponse response = new WeaponToGrabResponse(player, gainedWeapon, droppedWeapon, payingEmps, aborted);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Notifies the setting of the chosen empower
     * @param player is the player who wants to use the empower card
     * @param empower is the chosen empower
     * @param target is the target of the empower
     * @param position is the chosen position of the empower
     * @param payingColor is the paying color if the empower has a cost (as targeting scope)
     * @param payingEmp is the empower that the player uses to pay with if the empower has a cost (as targeting scope)
     * @param aborted is true if the player wants to abort the action
     */
    public void notifyEmpowerUseResponse(String player, EmpowerInfo empower, String target, Position position, it.polimi.ingsw.model.Color payingColor, EmpowerInfo payingEmp, boolean aborted) {
        EmpowerUseResponse response = new EmpowerUseResponse(player, empower, target, position, payingColor, payingEmp, aborted);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Notifies the basic effect settings
     * @param abort is true if the player wants to abort the action
     * @param player is the player that uses the basic effect
     * @param targets is the targets of the basic effect
     * @param positions are the chosen target position
     * @param recoilPos is the final position of the recoil if the effect has a recoil movement
     */
    public void notifyBasicEffectUseResponse(boolean abort, String player, List<String> targets, List<Position> positions, Position recoilPos) {
        BasicEffectUseResponse response = new BasicEffectUseResponse(abort, player, targets, positions, recoilPos);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Notifies the empower that the player drops to respawn
     * @param player is the player who respawns
     * @param empower is the dropped empower
     */
    public void notifyRespawnResponse(String player, EmpowerInfo empower) {
        RespawnResponse response = new RespawnResponse(player, empower);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Notifies the weapons the the player wants to reload
     * @param player is the player who wants to reload
     * @param weapons is the chosen weapons to reloads
     * @param payingEmps are the empowers that the player uses to pay with
     * @param abort is true if the player wants to abort the action
     */
    public void notifyWeaponToReloadResponse(String player, List<WeaponInfo> weapons, List<EmpowerInfo> payingEmps, boolean abort) {
        WeaponsToReloadResponse response = new WeaponsToReloadResponse(player, weapons, payingEmps, abort);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Notifies the settings of the ultra damage
     * @param abort is true if the player wants to abort the action
     * @param player is the player who uses the ultra effect
     * @param targets are the targets of the ultra effect
     * @param position is the chosen target position of the ultra effect
     */
    public void notifyUltraEffectUseResponse(boolean abort, String player, List<String> targets, Position position) {
        UltraEffectUseResponse response = new UltraEffectUseResponse(abort, player, targets, position);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Notifies the chosen ultra effect
     * @param player is the player who wants to use the ultra effect
     * @param aborted is true if the player wants to abort the action
     * @param resp is true if the player wants to use the ultra effect
     * @param empsToPay are the empowers that the player uses to pay with
     */
    public void notifylUltraEffectResponse(String player, boolean aborted, boolean resp, List<EmpowerInfo> empsToPay) {
        UltraEffectResponse response = new UltraEffectResponse(player, aborted, resp, empsToPay);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Notify the shooter movement settings
     * @param player the player who wants to use the shooter movement effect
     * @param abort is true if the player wants to abort the action
     * @param position is the position where the player wants to move
     * @param firstBase is true if the player wants to use the shooter movement effect before the basic effect
     * @param payingEmps are the empowers that the player uses to pay with
     */
    public void notifyShooterMovementResponse(String player, boolean abort, Position position, boolean firstBase, List<EmpowerInfo> payingEmps) {
        ShooterMovementResponse response = new ShooterMovementResponse(player, abort, position, firstBase, payingEmps);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Notifies the confirm of the choice to grab an ammunition card
     * @param playerName the player who is grabbing an ammunition card
     * @param abort is true if the player wants to abort the action
     */
    public void notifyGrabAmmoResponse(String playerName, boolean abort) {
        GrabAmmoResponse response = new GrabAmmoResponse(playerName, abort);
        new Thread(() -> asyncSendEvent(response)).start();
    }

    /**
     * Pings the server to check the connection
     */
    public void pingServer() {
        new Thread(() -> asyncSendEvent(new PingServer(PingServer.OK))).start();
    }
}
