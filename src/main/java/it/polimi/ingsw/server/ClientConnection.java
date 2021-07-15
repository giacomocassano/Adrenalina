package it.polimi.ingsw.server;

import it.polimi.ingsw.client.user_interfaces.model_representation.*;
import it.polimi.ingsw.events.client_to_server.to_controller.EventFromView;
import it.polimi.ingsw.events.client_to_server.to_server.ConfigEvent;
import it.polimi.ingsw.events.client_to_server.to_server.PingServer;
import it.polimi.ingsw.events.client_to_server.to_server.SelectedName;
import it.polimi.ingsw.events.model_to_view.*;
import it.polimi.ingsw.events.visitors.ConfigVisitor;
import it.polimi.ingsw.events.visitors.ServerVisitor;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.AmmunitionCard;
import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.EmpowerType;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.model.cards.effects.ShootEffect;
import it.polimi.ingsw.model.cards.effects.ShooterMovement;
import it.polimi.ingsw.model.cards.effects.UltraDamage;
import it.polimi.ingsw.server.server_thread.PingResponseThread;
import it.polimi.ingsw.server.server_thread.PingThread;
import it.polimi.ingsw.utils.EventsFromViewReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class that represent the client connection on server side.
 * It's used to send and receive events from client.
 */
public abstract class ClientConnection implements ConfigVisitor, ServerVisitor {

    private Server server;
    private boolean active;
    private EventFromModel lastEventSent;

    private PingThread pingThread;
    private PingResponseThread pingResponseThread;

    private EventsFromViewReceiver eventsReceiver;

    /**
     * Constructor.
     * @param server is the server reference.
     */
    public ClientConnection(Server server) {
        this.active = true;
        this.lastEventSent = null;
        this.server = server;
    }

    /**
     * Method to send event to client in a separate thread.
     * @param event is the event to send.
     */
    public abstract void asyncSendEvent(EventFromModel event);

    public void startPingThread(String name) {
        this.pingThread = new PingThread(this);
        pingResponseThread = new PingResponseThread(this, name);
        new Thread(pingThread).start();
        new Thread(pingResponseThread).start();
    }

    /**
     * Setter method.
     * @param eventsReceiver is the receiver to set.
     */
    public void registerEventsReceiver(EventsFromViewReceiver eventsReceiver) {
        this.eventsReceiver = eventsReceiver;
    }

    /**
     * Disconnect client reference in server side applications.
     */
    public void disconnectConnectionFromServer() {
        server.setConnectionAsInactive(this);
        if(getPingThread() != null)
            getPingThread().setActive(false);
    }


    /**
     * Gets last event sended.
     * @return last event sended.
     */
    public EventFromModel getLastEventSent() {
        return lastEventSent;
    }

    /**
     * Setter.
     * @param lastEventSent is the event to set.
     */
    public void setLastEventSent(EventFromModel lastEventSent) {
        this.lastEventSent = lastEventSent;
    }

    /**
     * Getter.
     * @return true if connection is active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Setter.
     * @param active is true if connection is active.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Getter of ping thread.
     * @return the ping thread.
     */
    public PingThread getPingThread() {
        return pingThread;
    }

    /* Notify methods */

    /**
     * Sends a spawn request to client.
     * @param activePlayer is the active player.
     */
    public void notifySpawnRequest(Player activePlayer) {
        List<EmpowerInfo> empsToChoose = activePlayer.getEmpowers().stream()
                .map(EmpowerCard::getEmpowerInfo)
                .collect(Collectors.toList());
        asyncSendEvent(new SpawnRequest(activePlayer.getName(), empsToChoose));
    }

    /**
     * Sends a move request to client.
     * @param activePlayer is the active player.
     * @param isFinalFrenzy is true if final frenzy is active.
     * @param validMoves are the valid moves.
     * @param actionNumber is the action number of the turn.
     * @param moveNumber is the move number of the action.
     */
    public void notifyMoveRequest(Player activePlayer, boolean isFinalFrenzy, List<MoveType> validMoves, int actionNumber, int moveNumber) {
        MoveRequest request = new MoveRequest(isFinalFrenzy, activePlayer.getName(), validMoves, actionNumber, moveNumber);
        asyncSendEvent(request);
    }

    /**
     * Sends a square to move request to client.
     * @param activePlayer is the active player.
     * @param map is the game map.
     */
    public void notifySquareToMoveRequest(Player activePlayer, Map map) {
        Square playerPosition = map.getPlayerPosition(activePlayer);
        List<Square> squares = map.getNearSquares(playerPosition);
        squares.add(playerPosition);
        List<Position> positions = squares.stream().map(Square::getPosition).collect(Collectors.toList());
        SquareMoveRequest request = new SquareMoveRequest(activePlayer.getName(), positions, playerPosition.getPosition());
        asyncSendEvent(request);
    }

    /**
     * Sends a gram ammo confirm request to client.
     * @param activePlayer is the active player.
     * @param map is the game map.
     */
    public void notifyGrabAmmoRequest(Player activePlayer, Map map) {
        Square position = map.getPlayerPosition(activePlayer);
        if (position != null && !position.isRespawnPoint() && position.getAmmos() != null) {
            AmmunitionCard ammunitionCard = position.getAmmos();
            GrabAmmoRequest request =  new GrabAmmoRequest(activePlayer.getName(), ammunitionCard.getAmmoCubes(), ammunitionCard.hasEmpower());
            asyncSendEvent(request);
        }
    }

    /**
     * Sends a end game notification to client.
     * @param player is the active player.
     * @param winner is the winner player.
     * @param tiePlayers are the parity players.
     * @param ranking is the final ordered ranking.
     */
    public void notifyEndGame(Player player, Player winner, List<Player> tiePlayers, java.util.Map<String, Integer> ranking) {
        List<String> ties = tiePlayers.stream().map(Player::getName).collect(Collectors.toList());
        asyncSendEvent(new GameOverNotification(player.getName(), winner.getName(), winner.getColor(), ties, ranking));
    }

    /**
     * Sends a grab weapon request to client.
     * @param activePlayer is the active player.
     * @param map is the game map.
     */
    public void notifyGrabWeaponRequest(Player activePlayer, Map map) {
        Square playerPosition = map.getPlayerPosition(activePlayer);
        List<WeaponInfo> weaponsList = playerPosition.getWeapons().stream()
                .filter(w -> activePlayer.canPay(w.getBuyCost()))
                .map(WeaponCard::getWeaponInfo)
                .collect(Collectors.toList());
        List<WeaponInfo> weaponsToDrop = new ArrayList<>();
        if (activePlayer.getWeapons().size() >= 3) {
            weaponsToDrop = activePlayer.getWeapons().stream()
                    .map(WeaponCard::getWeaponInfo)
                    .collect(Collectors.toList());
        }
        List<EmpowerInfo> payingEmps = activePlayer.getEmpowers().stream().map(EmpowerCard::getEmpowerInfo).collect(Collectors.toList());
        //Build and send the event
        WeaponToGrabRequest event = new WeaponToGrabRequest();
        event.setPlayer(activePlayer.getName());
        event.setWeaponsToGrab(weaponsList);
        event.setWeaponsToDrop(weaponsToDrop);
        event.setAmmoCubes(activePlayer.getAmmoCubes());
        event.setPayingEmpowers(payingEmps);
        asyncSendEvent(event);
    }

    /**
     * Sends a shoot weapon request to client.
     * @param activePlayer is the active player.
     */
    public void notifyShootWeaponRequest(Player activePlayer) {
        List<WeaponInfo> weapons = activePlayer.getWeapons().stream()
                .filter(WeaponCard::isLoaded)
                .map(WeaponCard::getWeaponInfo)
                .collect(Collectors.toList());
        ShootWeaponRequest request = new ShootWeaponRequest(activePlayer.getName(), weapons);
        asyncSendEvent(request);
    }

    /**
     * Sends a weapons to reload request.
     * @param activePlayer is the active player.
     */
    public void notifyWeaponsToReload(Player activePlayer) {
        List<WeaponInfo> weaponsToReload = activePlayer.getWeapons().stream()
                .filter(w -> !w.isLoaded())
                .filter(w -> activePlayer.canPay(w.getReloadCost()))
                .map(WeaponCard::getWeaponInfo)
                .collect(Collectors.toList());
        List<EmpowerInfo> payingEmpowers = activePlayer.getEmpowers().stream().map(EmpowerCard::getEmpowerInfo).collect(Collectors.toList());
        ///Build request event
        WeaponsToReloadRequest request = new WeaponsToReloadRequest();
        request.setPlayer(activePlayer.getName());
        request.setWeaponsToReload(weaponsToReload);
        request.setAmmoCubes(activePlayer.getAmmoCubes());
        request.setPayingEmpowers(payingEmpowers);
        asyncSendEvent(request);
    }

    /**
     * Sends a message to client.
     * @param message is the info message.
     */
    public void notifyMessage(String message) {
        asyncSendEvent(new MessageNotification(MessageNotification.INFO, message));
    }

    /**
     * Sends an error message to client.
     * @param message is the error message.
     */
    public void notifyErrorMessage(String message) {
        asyncSendEvent(new MessageNotification(MessageNotification.ERROR, message));
    }

    /**
     * Sends an empower request to client.
     * @param player is player reference.
     * @param valids are valid empowers.
     */
    public void notifyEmpowerRequest(Player player, List<EmpowerType> valids) {
        List<EmpowerInfo> validEmps = player.getEmpowers().stream()
                .filter(e -> valids.contains(e.getType()))
                .map(EmpowerCard::getEmpowerInfo)
                .collect(Collectors.toList());
        asyncSendEvent(new EmpowerRequest(player.getName(), validEmps));
    }

    /**
     * Sends a empower use request to client.
     * @param activePlayer is the active player.
     * @param player is the player who uses the empower.
     * @param map is the game map.
     * @param players are the game players.
     * @param empower is the selected empower.
     */
    public void notifyEmpowerUseRequest(Player activePlayer, Player player, Map map, List<Player> players, EmpowerCard empower) {
        EmpowerUseRequest request = new EmpowerUseRequest(player.getName());
        request.setEmpower(empower.getEmpowerInfo());
        List<String> targets = new ArrayList<>();
        List<Position> possiblePositions;
        switch (empower.getType()) {
            case TARGETING_SCOPE:
                request.setPayable(true);
                request.setAmmoCubes(player.getAmmoCubes());
                targets = players.stream().filter(Player::isJustShot).filter(p -> !p.isDead()).map(Player::getName).collect(Collectors.toList());
                List<EmpowerInfo> payingEmps = player.getEmpowers().stream().filter(e -> e.getId() != empower.getId()).map(EmpowerCard::getEmpowerInfo).collect(Collectors.toList());
                request.setPossibleTargets(targets);
                request.setPayingEmpowers(payingEmps);
                break;
            case TELEPORTER:
                request.setPayable(false);
                possiblePositions = map.getEverySquares().stream().map(Square::getPosition).collect(Collectors.toList());
                request.setPossiblePositions(possiblePositions);
                break;
            case TAGBACK_GRANADE:
                request.setPayable(false);
                targets.add(activePlayer.getName());
                request.setPossibleTargets(targets);
                break;
            case NEWTON:
                request.setPayable(false);
                targets = players.stream().filter(p -> p != player).filter(p -> !p.isDead()).map(Player::getName).collect(Collectors.toList());
                possiblePositions = map.getEverySquares().stream().map(Square::getPosition).collect(Collectors.toList());
                request.setPossibleTargets(targets);
                request.setPossiblePositions(possiblePositions);
                break;
        }
        asyncSendEvent(request);
    }

    /**
     * Sends a shooter movement request to client.
     * @param player is the active player.
     * @param map is the game map.
     * @param shooterMovement is the active shooter movement effect.
     */
    public void notifyShooterMovementRequest(Player player, Map map, ShooterMovement shooterMovement){
        EffectInfo info = shooterMovement.getEffectInfo();
        List<EmpowerInfo> payingEmps = player.getEmpowers().stream().map(EmpowerCard::getEmpowerInfo).collect(Collectors.toList());
        Square playerPos = map.getPlayerPosition(player);
        List<Position> possiblePos = new ArrayList<>();
        if (playerPos != null)
            possiblePos = map.getSquareAtMaxDistance(playerPos, shooterMovement.getMaxSteps()).stream().map(Square::getPosition).collect(Collectors.toList());
        asyncSendEvent(new ShooterMovementRequest(player.getName(), info, possiblePos, payingEmps));
    }

    /**
     * Sends a basic effect request to client.
     * @param activePlayer is the active player.
     * @param activeWeapon is the active weapon.
     */
    public void notifyBasicEffectRequest(Player activePlayer, WeaponCard activeWeapon) {
        List<EffectInfo> basicEffects = activeWeapon.getBasicEffects().stream()
                .filter(e -> !activePlayer.getAmmoCubes().isLowerThan(e.getCost()))
                .map(ShootEffect::getEffectInfo)
                .collect(Collectors.toList());
        List<EmpowerInfo> payingEmps = activePlayer.getEmpowers().stream().map(EmpowerCard::getEmpowerInfo).collect(Collectors.toList());
        //Build the event
        BasicEffectRequest request = new BasicEffectRequest();
        request.setPlayer(activePlayer.getName());
        request.setBasicEffects(basicEffects);
        request.setAmmoCubes(activePlayer.getAmmoCubes());
        request.setPayingEmpowers(payingEmps);
        asyncSendEvent(request);
    }

    /**
     * Sends a respawn request to dead player.
     * @param dead is the dead player.
     */
    public void notifyRespawnRequest(Player dead) {
        List<EmpowerInfo> emps = dead.getEmpowers().stream().map(EmpowerCard::getEmpowerInfo).collect(Collectors.toList());
        RespawnRequest request = new RespawnRequest(dead.getName(), emps);
        asyncSendEvent(request);
    }

    /**
     * Sends basic effect use request to client.
     * @param activePlayer is the active player.
     * @param map is the game map.
     * @param effect is the shoot effect.
     */
    public void notifyBasicEffectUseRequest(Player activePlayer, Map map, ShootEffect effect) {
        List<String> targets = effect.getPossibleTargets(map, activePlayer).stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        List<Position> squares = effect.getPossibleSquares(map, activePlayer).stream()
                .map(Square::getPosition)
                .collect(Collectors.toList());
        List<Position> recoilPos = new ArrayList<>();
        if (effect.getRecoil() != null) {
            recoilPos = map.getPossibleMovementSquares(effect.getPossibleTargets(map, activePlayer).get(0), effect.getRecoil().getMaxSteps())
                    .stream().map(Square::getPosition).collect(Collectors.toList());
        }

        BasicEffectUseRequest request = new BasicEffectUseRequest();
        request.setPlayer(activePlayer.getName());
        request.setMaxPlayers(effect.getNumTargetsToSelect());
        request.setMaxSquares(effect.getNumSquaresToSelect());
        request.setPlayerNames(targets);
        request.setSquarePositions(squares);
        request.setRecoilPositions(recoilPos);
        asyncSendEvent(request);
    }

    /**
     * Sends a ultra effect request.
     * @param activePlayer is the active player.
     * @param ultra is the ultra effect.
     */
    public void notifyUltraEffectRequest(Player activePlayer, UltraDamage ultra) {
        List<EmpowerInfo> empowers = activePlayer.getEmpowers().stream().map(EmpowerCard::getEmpowerInfo).collect(Collectors.toList());
        UltraEffectRequest request = new UltraEffectRequest(activePlayer.getName(), ultra.getEffectInfo(), empowers);
        asyncSendEvent(request);
    }

    /**
     * Sends to a player an Ultra effect use request
     * @param player is the player to send the event
     * @param map is the playing map
     * @param ultra is the effect to send.
     */
    public void notifyUltraEffectUseRequest(Player player, Map map, UltraDamage ultra) {
        List<String> targets = ultra.getPossibleTargets(map, player).stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        List<Position> positions = ultra.getPossibleSquares(map, player).stream().map(Square::getPosition).collect(Collectors.toList());
        //Send ultra effect use request
        UltraEffectUseRequest request = new UltraEffectUseRequest();
        request.setPlayer(player.getName());
        request.setPlayerNames(targets);
        request.setSquarePositions(positions);
        request.setMaxPlayers(ultra.getNumTargetsToSelect());
        request.setMaxSquares(ultra.getNumSquaresToSelect());
        asyncSendEvent(request);
    }

    /**
     * Notify to a player that time to do something is over
     * @param message is the time over message to send
     */
    public void notifyTimeExceeded(String message) {
        asyncSendEvent(new TimeExceededNotification(message));
    }


    /**
     * Sends to a player a representation about the actual game
     * @param player is the player to send the info
     * @param game is the actual game
     * @param gameStarting is true if game is starting
     */
    public void notifyGameRepContext(Player player, Game game, boolean gameStarting) {
        GameRepUpdate gameRep = new GameRepUpdate();
        gameRep.setGameStarting(gameStarting);
        gameRep.setMyPlayerRep(player.getMyPlayerRepresentation(game.getMap()));
        List<PlayerRepresentation> otherReps = game.getPlayers().stream()
                .filter(p -> !p.getName().equals(player.getName()))
                .map(p -> p.getPlayerRepresentation(game.getMap()))
                .collect(Collectors.toList());
        gameRep.setOtherPlayersReps(otherReps);
        gameRep.setMapRep(game.getMap().getMapRepresentation());
        gameRep.setGameBoardRepresentation(buildGameBoard(game));
        asyncSendEvent(gameRep);
    }

    /**
     * Notifies to a player that the login is completed with success
     * @param type is the type of the login success
     * @param player is the connected player
     * @param welcomeMessage is a welcome message
     */
    public void notifyLoginSuccess(int type, String player, String welcomeMessage) {
        LoginSuccess loginSuccess = new LoginSuccess(type, player, welcomeMessage);
        asyncSendEvent(loginSuccess);
    }

    /**
     * Notifies to a player that there was an error during login
     * @param type is the type of the error
     * @param player is the player that has an error.
     */
    public void notifyLoginError(int type, String player) {
        LoginError loginError = new LoginError(type, player);
        asyncSendEvent(loginError);
    }

    public void notifyLoginRequest() {
        asyncSendEvent(new LoginRequest());
    }

    /**
     * Notify clients that waiting room has been updated
     * @param connectedPlayer is the player that connected
     * @param disconnectedPlayer is the player that disconnected
     * @param waitingPlayers are players that are waiting in the lobby
     * @param isStarting is true if game is starting
     * @param timer is time before game starts
     */
    public void notifyUpdateWaitingRoom(String connectedPlayer, String disconnectedPlayer, List<String> waitingPlayers, boolean isStarting, int timer) {
        WaitingRoomUpdate update = new WaitingRoomUpdate(connectedPlayer, disconnectedPlayer, waitingPlayers, isStarting, timer);
        asyncSendEvent(update);
    }

    /**
     * Notify to clients that a certain player has disconnected
     * @param disconnectedPlayer is the player that has disconnected
     */
    public void notifyDisconnection(String disconnectedPlayer) {
        DisconnectionNotification notification = new DisconnectionNotification(disconnectedPlayer);
        asyncSendEvent(notification);
    }

    /**
     * Notify to clients that a certain player is reconnected
     * @param reconnectedPlayer is the player that has reconnected
     * @param gameClients are other players
     */
    public void notifyReconnection(Player reconnectedPlayer, List<String> gameClients) {
        ReconnectionNotification notification = new ReconnectionNotification(reconnectedPlayer.getName(), gameClients);
        asyncSendEvent(notification);
    }

    /**
     * sends a ping to client
     */
    public void pingClient() {
        asyncSendEvent(new PingClient());
    }

    /**
     * Creates a GameBoardRepresentation starting from game;
     * @param game is the actual game
     * @return the gameboard representation
     */
    private GameBoardRepresentation buildGameBoard(Game game) {
        GameBoardRepresentation board = new GameBoardRepresentation();
        board.setAmmoStackSize(game.getAmmoStack().getSize());
        board.setEmpowerStackSize(game.getEmpowerStack().getSize());
        board.setWeaponsStackSize(game.getWeaponStack().getSize());
        board.setKillshotTrackRep(game.getKillshotTrack().getKillShotRep());
        return board;
    }

    /* VISIT METHOD AS SERVER VISITOR */

    /**
     * Visit method that recives an EventFromView event
     * @param event is the event to visit.
     */
    @Override
    public void visit(EventFromView event) {
        eventsReceiver.receiveEvent(event);
    }

    /**
     * Visit Method of config event
     * @param event is the event to visit.
     */
    @Override
    public void visit(ConfigEvent event) {
        event.acceptConfigVisitor(this);
    }

    /* VISIT METHODS AS CONFIG VISITOR */

    /**
     * Visit method of SelectedName event
     * @param event is the event to visit.
     */
    @Override
    public void visit(SelectedName event) {
        String name = event.getName().toUpperCase();
        if(server.isNameAlreadyExisting(name)) {
            asyncSendEvent(new LoginError(LoginError.NAME_ALREADY_EXISTING, name));
        }else{
            server.rendezvous(this, event.getName());
        }
    }

    /**
     * Visit method of PingServer event.
     * @param ping is the event to visit.
     */
    @Override
    public void visit(PingServer ping) {
        if(ping.getResponse() == PingServer.OK)
            this.pingResponseThread.setConnected(true);
    }

}
