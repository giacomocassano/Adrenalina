package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.controller.controller_thread.WaitingResponseThread;
import it.polimi.ingsw.events.client_to_server.to_controller.*;
import it.polimi.ingsw.events.visitors.ControllerVisitor;
import it.polimi.ingsw.exceptions.EffectException;
import it.polimi.ingsw.exceptions.MoveException;
import it.polimi.ingsw.exceptions.ShootException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.MoveType;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.EmpowerType;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.model.cards.effects.ShootEffect;
import it.polimi.ingsw.utils.GsonHelper;
import it.polimi.ingsw.utils.RepresentationHelper;
import it.polimi.ingsw.utils.ViewObserver;
import it.polimi.ingsw.utils.WriterHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Controller class of Model View Controller pattern.
 */
public class Controller implements ViewObserver, ControllerVisitor {

    private static final int WAITING_SPAWN_SECONDS = 30;
    private static final int WAITING_MOVE_SECONDS = 99;
    private static final int WAITING_EMP_SECONDS = 90;

    private final Game game;
    private String path;

    //Waiting threads
    private WaitingResponseThread waitingSpawnThread;
    private WaitingResponseThread waitingMoveThread;
    private WaitingResponseThread waitingEmpowersThread;
    private WaitingResponseThread waitingDeathsThread;

    /**
     * Constructor
     * @param game is the game class instance
     */
    public Controller(Game game) {
        this.game = game;
    }

    /**
     * Starts the game.
     */
    public void startGame() {
        game.getActiveTurn().startTurn();
        game.updateGameRepContext(true);
        //Request the born
        game.firstBorn();
        //Start the waitingSpawnThread
        startWaitingSpawnThread();
    }

    /**
     * Restarts the game.
     * Persistence method.
     */
    public void restartGame() {
        //Start the turn
        startNewTurn(false);
    }

    /**
     * Controls if controller is waiting a move.
     * @return true if controller is waiting a move.
     */
    public boolean isWaitingMove() {
        if(waitingMoveThread != null)
            return waitingMoveThread.isWaiting();
        return false;
    }

    /**
     * Controls if controller is waiting a spawn.
     * @return true if controller is waiting a spawn.
     */
    public boolean isWaitingSpawn() {
        if(waitingSpawnThread != null)
            return waitingSpawnThread.isWaiting();
        return false;
    }

    /**
     * Controls if controller is waiting an empower choice.
     * @return true if controller is waiting an empower choice.
     */
    public boolean isWaitingEmpower() {
        if(waitingEmpowersThread != null)
            return waitingEmpowersThread.isWaiting();
        return false;
    }

    /**
     * Controls if controller is waiting a respawn.
     * @return true if controller is waiting a respawn.
     */
    public boolean isWaitingRespawn() {
        if(waitingDeathsThread != null)
            return waitingDeathsThread.isWaiting();
        return false;
    }

    /**
     * Control if an action is ended.
     * If action is ended, it asks for the empowers.
     * Else it asks for a new move.
     */
    private void controlEndOfAction() {
        if(game.getActiveTurn().getActiveAction().isActive()) { //Action is not ended
            game.nextMove();
        }else{ //Action is ended
            onActionEnded();
        }
    }

    /**
     * Setter
     * @param path is the parameter to set.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Control if the turn is ended.
     * Stops the threads.
     * If turn is ended, it ends the turn.
     * It asks the empower uses.
     */
    private void onActionEnded() {
        //Remove player from waiting move thread
        if(waitingMoveThread.isWaiting())
            waitingMoveThread.removeWaitedPlayer(game.getActivePlayer());
        //It turn is ended
        if(game.getActiveTurn().getRemainingActions() == 1)
            game.getActiveTurn().endTurn();
        //Request empower use
        game.requestEmpowerUses(game.getPlayers());
        startWaitingEmpowersThread();
    }

    /**
     * Method called at the end of empower uses process.
     * Checks the dead. If there are dead player, it asks for their respawns.
     * Else start a new action.
     */
    private void controlEndOfEmpowerUses() {
        if(game.getActiveTurn().getRemainingActions() == 2) { //il giocatore ha fatto solo la prima delle 2 azioni
            game.getActiveTurn().setRemainingActions(1);
            game.nextAction();
            game.nextMove();
            startWaitingMoveThread();
        }else{ //il giocatore ha fatto la seconda delle due azioni, è finito il suo turno
            //Control deaths
            game.checkDeaths();
            //Control if game is ended
            if(game.isFinalFrenzy() && game.getActivePlayer().isFirstFinalFrenzy() && game.isLastRound()) {
                game.endGame();
            }else { //if game is not endend
                List<Player> deadPlayers = game.getDeadPlayers();
                if (deadPlayers.isEmpty()) {
                    startNewTurn(true);
                } else {
                    game.handleDeaths();
                    startWaitingDeathsThread();
                }
            }
        }
        //Reset shoot players
        game.resetShootedPlayers();
    }

    /**
     * Start a new turn.
     * @param updatePlayer is true if the controller has to change the active player.
     */
    private void startNewTurn(boolean updatePlayer) {
        if(game.isFinalFrenzy())
            game.setLastRound(true);
        game.getActiveTurn().startTurn();
        game.getActiveTurn().setRemainingActions(2);
        game.refillMap(); //On the end of the turn the controller must refill the map
        //If updatePlayer is true, the game goes normally, but if updatePlayer is false te game is loaded with persistence functions
        if(updatePlayer) {
            game.updateActivePlayer();
            game.updateGameRepContext(false);
            //Save the game
            if(path != null && !game.isFirstRound())
                GsonHelper.writeGameStatus(game, path);
        }else{
            game.updateGameRepContext(true);
        }
        game.nextAction();
        if(game.isFirstRound()) {
            game.firstBorn();
            startWaitingSpawnThread();
        }else {
            game.nextMove();
            startWaitingMoveThread();
        }
    }

    /**
     * Signals that the time to perform something is exceeded.
     * @param players are the players that exceed the time.
     */
    public void responseTimeExceeded(List<Player> players) {
        game.timeExceeded(players);
    }


    /**
     * Starts the waiting spawn thread.
     */
    private void startWaitingSpawnThread() {
        //Build the list of waiting players
        Player player = game.getActivePlayer();
        List<Player> waitedPlayers = new ArrayList<>();
        waitedPlayers.add(player);
        //Build the defaultEvent, choosing random empower
        List<EmpowerInfo> emps = player.getEmpowers().stream().map(EmpowerCard::getEmpowerInfo).collect(Collectors.toList());
        EmpowerInfo empToDrop = emps.get(new Random().nextInt(emps.size()));
        emps.remove(empToDrop);
        FirstSpawnResponse defaultEvent = new FirstSpawnResponse(player.getName(), empToDrop, emps);
        //Create and start the waiting thread
        waitingSpawnThread = new WaitingResponseThread("SpawnThread", this, waitedPlayers, defaultEvent, WAITING_SPAWN_SECONDS);
        waitingSpawnThread.start();
    }

    /**
     * Starts the waiting move thread.
     */
    private void startWaitingMoveThread() {
        Player player = game.getActivePlayer();
        //Build the list of waiting players
        List<Player> waitedPlayers = new ArrayList<>();
        waitedPlayers.add(player);
        //Build the default event
        MoveExceeded defaultEvent = new MoveExceeded();
        //Create and start the waiting thread
        waitingMoveThread = new WaitingResponseThread("MoveThread",this, waitedPlayers, defaultEvent, WAITING_MOVE_SECONDS);
        waitingMoveThread.start();
    }

    /**
     * Starts the waiting empower thread.
     */
    private void startWaitingEmpowersThread() {
        //Build the list of waiting players
        List<Player> waitedPlayers = game.getActiveTurn().getActiveEmpowerPlayers();
        //Build the default event
        EmpowerUseExceeded defaultEvent = new EmpowerUseExceeded();
        //Create and start the waiting thread
        waitingEmpowersThread = new WaitingResponseThread("EmpowersThread", this, waitedPlayers, defaultEvent, WAITING_EMP_SECONDS);
        waitingEmpowersThread.start();
    }

    /**
     * Starts the waiting death thread.
     */
    private void startWaitingDeathsThread() {
        //Build the list of waiting players
        List<Player> waitedPlayers = game.getDeadPlayers();
        //Build the default event
        DeathTimeExceeded defaultEvent = new DeathTimeExceeded();
        //Create and start the waiting thread
        waitingDeathsThread = new WaitingResponseThread("DeathThread",this, waitedPlayers, defaultEvent, WAITING_SPAWN_SECONDS);
        waitingDeathsThread.start();
    }

    /**
     * Update the controller with an event from view.
     * @param eventFromView is the update event.
     */
    @Override
    public void update(EventFromView eventFromView) {
        eventFromView.acceptControllerVisitor(this);
    }

    /**
     * Handle the fist spawn of a player.
     * @param event to visit.
     */
    @Override
    public void visit(FirstSpawnResponse event) {
        //Remove player from waiting thread
        if(waitingSpawnThread.isWaiting()) {
            waitingSpawnThread.removeWaitedPlayer(game.getActivePlayer());
        }
        //Perform the spawn into the model
        EmpowerInfo empToDrop = event.getDroppedEmpower();
        EmpowerCard dropped = null;
        for(EmpowerCard emp: game.getActivePlayer().getEmpowers())
            if(emp.getId() == empToDrop.getId())
                dropped = emp;
        game.performFirstSpawn(dropped);
        //Update context to client
        game.updateGameRepContext(false);
        //Create a new action and request a move to active player
        game.nextAction();
        game.nextMove();
        //Start the waiting move thread
        startWaitingMoveThread();
    }

    /**
     * Handle the move response of a player.
     * @param event to visit.
     */
    @Override
    public void visit(MoveResponse event) {
        //Handle the move
        MoveType move = event.getMoveType();
        game.handleMove(move);
        //Control if action is ended
        if(!game.getActiveTurn().getActiveAction().isActive()) {
            onActionEnded();
        }
    }

    /**
     * Handle the empower response of a player.
     * @param event to visit.
     */
    @Override
    public void visit(EmpowerResponse event) {
        Player player = RepresentationHelper.playerMapper(game.getPlayers(), event.getPlayer());
        //Handle empower use
        if(!event.isWantUse()) {
            if(waitingEmpowersThread.isWaiting()) {
                waitingEmpowersThread.removeWaitedPlayer(player);
                game.getActiveTurn().removeEmpowerPlayer(player);
                if(game.getActiveTurn().getActiveEmpowerPlayers().isEmpty())
                    controlEndOfEmpowerUses();
            }
        }else{
            EmpowerCard empowerCard = RepresentationHelper.empowerMapper(player.getEmpowers(), event.getEmpower());
            if(empowerCard != null)
                game.handleEmpower(player, empowerCard);
            else
                WriterHelper.printErrorMessage("Il giocatore non ha quel potenziamento.");
        }
    }

    /**
     * Handle the empower use response of a player.
     * @param response to visit.
     */
    @Override
    public void visit(EmpowerUseResponse response) {
        Player player = RepresentationHelper.playerMapper(game.getPlayers(), response.getPlayer());
        if(response.isAbort()) {
            List<Player> players = new ArrayList<>();
            players.add(player);
            game.requestEmpowerUses(players);
        }else{
            EmpowerCard empowerCard = RepresentationHelper.empowerMapper(player.getEmpowers(), response.getEmpower());
            if(empowerCard.getType() == EmpowerType.TARGETING_SCOPE) {
                empowerCard.setCubeColor(response.getPayingColor());
                if(response.getPayingEmp() != null) {
                    EmpowerCard payingEemp = RepresentationHelper.empowerMapper(player.getEmpowers(), response.getPayingEmp());
                    player.addPayingEmpower(payingEemp);
                }
            }
            Player target = RepresentationHelper.playerMapper(game.getPlayers(), response.getTarget());
            Square position = RepresentationHelper.squareMapper(game.getMap().getEverySquares(), response.getPosition());
            //Controllo se è possibile usarlo
            if(game.isValidEmpowerUse(player, empowerCard, target, position))
                game.handleEmpowerUse(player, empowerCard, target, position);
            if(!game.getValidEmpowerTypes(player).isEmpty()) {
                List<Player> players = new ArrayList<>();
                players.add(player);
                game.requestEmpowerUses(players);
            }else{
                if(waitingEmpowersThread.isWaiting()) {
                    waitingEmpowersThread.removeWaitedPlayer(player);
                    game.getActiveTurn().removeEmpowerPlayer(player);
                    if(game.getActiveTurn().getActiveEmpowerPlayers().isEmpty())
                        controlEndOfEmpowerUses();
                }
            }
        }
    }

    /**
     * Handle the shooter movement use of a player.
     * @param response to visit.
     */
    @Override
    public void visit(ShooterMovementResponse response) {
        Player player = RepresentationHelper.playerMapper(game.getPlayers(), response.getPlayer());
        Square position = RepresentationHelper.squareMapper(game.getMap().getEverySquares(), response.getPosition());
        List<EmpowerCard> payingEmps = RepresentationHelper.empowersMapper(game.getActivePlayer().getEmpowers(), response.getPayingEmpower());
        player.addPayingEmpowers(payingEmps);
        int order = 1;
        if(response.isBeforeBasicEffect())
            order = 0;
        if(game.getActivePlayer() == player) {
            try {
                game.handleShooterMovement(game.getActiveTurn().getActiveWeapon().getShooterMovement(), response.isWantUse(), position, order);
            } catch (MoveException | ShootException | EffectException e) {
                WriterHelper.printErrorMessage("Errore nell'usare lo shooter movement");
                e.printStackTrace();
            }
            //Control end of action
            if(!game.isPlayerShooting())
                controlEndOfAction();
        }

    }

    /**
     * Called when move time exceeds.
     * It ends the current action and asks for the empowers.
     * @param event to visit.
     */
    @Override
    public void visit(MoveExceeded event) {
        //If move time exceed, then the action ends
        game.handleMove(MoveType.END_ACTION);
        //Update game context to clients
        game.updateGameRepContext(false);
        //Action is ended, so request empower use
        game.requestEmpowerUses(game.getPlayers());
        startWaitingEmpowersThread();
    }

    /**
     * Called when empower use time exceeds.
     * It start a new action or checks the dead players.
     * @param event to visit.
     */
    @Override
    public void visit(EmpowerUseExceeded event) {
        game.getActiveTurn().clearEmpowerPlayers();
        //Update game context to client
        game.updateGameRepContext(false);
        //Control end of empower uses
        controlEndOfEmpowerUses();
    }

    /**
     * Called when the time to respawn exceed.
     * It respawn automatically the deads players and starts a new turn.
     * @param event to visit.
     */
    @Override
    public void visit(DeathTimeExceeded event) {
        List<Player> deadPlayers = new ArrayList<>(game.getDeadPlayers());
        for(Player dead: deadPlayers) {
            List<EmpowerCard> emps = dead.getEmpowers();
            EmpowerCard dropped = emps.get(new Random().nextInt(emps.size()));
            game.performRespawn(dead, dropped);
        }
        startNewTurn(true);
    }

    /**
     * Respawns the dead player.
     * @param response is the event to visit.
     */
    @Override
    public void visit(RespawnResponse response) {
        Player spawned = RepresentationHelper.playerMapper(game.getPlayers(), response.getPlayer());
        if(waitingDeathsThread.isWaiting()) {
            waitingDeathsThread.removeWaitedPlayer(spawned);
        }
        EmpowerCard dropped = RepresentationHelper.empowerMapper(spawned.getEmpowers(), response.getDropped());
        //Perform respawn
        game.performRespawn(spawned, dropped);
        if(game.getDeadPlayers().isEmpty()) {
            startNewTurn(true);
        }
    }

    /**
     * Handle the player movement.
     * @param response is the event to visit.
     */
    @Override
    public void visit(SquareToMoveResponse response) {
        if(response.isAborted()) {
            game.abortAction();
        }else{
            try {
                game.performMoveSingleSquarePlayer(game.getMap().getSquare(response.getPosition()));
                game.updateGameRepContext(false);
                controlEndOfAction();
            } catch (MoveException | EffectException | ShootException e) {
                WriterHelper.printErrorMessage("Errore nello spostamento del giocatore.");
            }
        }
    }

    /**
     * Handle the grab of an ammunition card.
     * @param response is the event to visit.
     */
    @Override
    public void visit(GrabAmmoResponse response) {
        if(response.isAborted()) {
            game.abortAction();
        }else{
            try {
                game.handleGrabAmmos();
                controlEndOfAction();
            } catch (MoveException | EffectException | ShootException e) {
                WriterHelper.printErrorMessage("Errore nello raccogliere le munizioni.");
            }
        }
    }

    /**
     * Handles the grab of a weapon card.
     * @param response is the event to visit.
     */
    @Override
    public void visit(WeaponToGrabResponse response){
        if(response.isAborted()) {
            game.abortAction();
        }else{
            //Handle the response
            Player player = game.getActivePlayer();
            Square position = game.getMap().getPlayerPosition(player);
            WeaponCard weaponToGrab = RepresentationHelper.weaponMapper(position.getWeapons(), response.getGainedWeapon());
            WeaponCard weaponToDrop = RepresentationHelper.weaponMapper(player.getWeapons(), response.getDroppedWeapon());
            List<EmpowerCard> payingEmps = RepresentationHelper.empowersMapper(player.getEmpowers(), response.getPayingEmpowers());
            //Perform model method handler
            player.addPayingEmpowers(payingEmps);
            try {
                game.handleGrabWeapon(weaponToGrab, weaponToDrop);
            } catch (MoveException | ShootException | EffectException e) {
                WriterHelper.printErrorMessage("Errore nel raccogliere l'arma.");
            }
            game.updateGameRepContext(false);
            controlEndOfAction();
        }
    }

    /**
     * Handles the reload of weapon cards.
     * @param response is the event to visit.
     */
    @Override
    public void visit(WeaponsToReloadResponse response) {
        if(response.isAborted()) {
            game.abortAction();
        }else{
            //Handle the response event
            Player player = game.getActivePlayer();
            List<WeaponCard> weapons = RepresentationHelper.weaponsMapper(player.getWeapons(), response.getWeaponsToReload());
            List<EmpowerCard> payingEmps = RepresentationHelper.empowersMapper(player.getEmpowers(), response.getPayingEmpowers());
            //Handle reload
            player.addPayingEmpowers(payingEmps);
            try {
                game.handleReload(weapons);
            } catch (MoveException | ShootException | EffectException e) {
                WriterHelper.printErrorMessage("Errore nel ricaricare le armi.");
            }
            game.updateGameRepContext(false);
            //Control end of action
            controlEndOfAction();
        }
    }

    /**
     * Handles the selection of the shoot weapon card.
     * @param response is the event to visit.
     */
    @Override
    public void visit(WeaponToShootResponse response) {
        if(response.isAborted()) {
            game.abortAction();
        }else {
            Player player = game.getActivePlayer();
            WeaponCard weaponCard = RepresentationHelper.weaponMapper(player.getLoadedWeapons(), response.getWeapon());
            game.handleWeaponToShoot(weaponCard);
            //No control end of action because controller is sure that game send the new event (that's to say BasicEffectRequest)
        }
    }

    /**
     * Handles the basic effect selection.
     * @param response is the event to visit.
     */
    @Override
    public void visit(BasicEffectResponse response) {
        if(response.isAborted()) {
            game.abortAction();
            if(game.getActiveTurn().getActiveWeapon() != null)
                game.getActiveTurn().getActiveWeapon().reset();
        }else{
            //Handle the response event
            Player player = game.getActivePlayer();
            ShootEffect shootEffect = RepresentationHelper.shootEffectMapper(game.getActiveTurn().getActiveWeapon().getBasicEffects(), response.getEffectInfo());
            List<EmpowerCard> payingEmps = RepresentationHelper.empowersMapper(player.getEmpowers(), response.getPayingEmpowers());
            //Perform model method handler
            player.addPayingEmpowers(payingEmps);
            game.handleBasicEffect(shootEffect);
            //No control end of action because controller is sure that game send the new event (that's to say BasicEffectUseRequest)
        }
    }

    /**
     * Handles the selection of basic effect use settings.
     * @param response is the event to visit.
     */
    @Override
    public void visit(BasicEffectUseResponse response) {
        if(response.isAborted()) {
            game.abortAction();
            if(game.getActiveTurn().getActiveWeapon() != null)
                game.getActiveTurn().getActiveWeapon().reset();
        }else{
            //Handle the response event
            List<Player> targets = RepresentationHelper.playersMapper(game.getPlayers(), response.getPlayerNames());
            List<Square> squares = RepresentationHelper.squaresMapper(game.getMap().getEverySquares(), response.getSquarePositions());
            Square recoilSquare = RepresentationHelper.squareMapper(game.getMap().getEverySquares(), response.getRecoilPosition());
            //Perform model method handler
            try {
                game.handleBasicEffectUse(targets, squares, recoilSquare);
            } catch (MoveException | ShootException | EffectException e) {
                WriterHelper.printErrorMessage("Errore nel settare l'effetto.");
            }
            //Control end of action
            if(!game.isPlayerShooting())
                controlEndOfAction();
            //else no control end of action because controller is sure that game send the new event (that's to say BasicEffectUseRequest)
        }
    }

    /**
     * Handles the ultra effect selection.
     * @param response is the event to visit
     */
    @Override
    public void visit(UltraEffectResponse response) {
        if (response.isAborted()) {
            game.abortAction();
            if(game.getActiveTurn().getActiveWeapon() != null)
                game.getActiveTurn().getActiveWeapon().reset();
        } else {
            try {
                game.handleUltraEffect(response.getResponse());
            } catch (MoveException | ShootException | EffectException e) {
                WriterHelper.printErrorMessage("Errore nel settare l'ultra effetto");
                e.printStackTrace();
            }
        }
        if(!response.getResponse())
            controlEndOfAction();
    }

    /**
     * Handles the ultra effect selection settings.
     * @param response is the event to visit.
     */
    @Override
    public void visit(UltraEffectUseResponse response) {
        if (response.isAborted()) {
            game.abortAction();
            if(game.getActiveTurn().getActiveWeapon() != null)
                game.getActiveTurn().getActiveWeapon().reset();
        } else {
            List<Player> targets = RepresentationHelper.playersMapper(game.getPlayers(), response.getPlayerNames());
            Square square = RepresentationHelper.squareMapper(game.getMap().getEverySquares(), response.getPosition());
            try {
                game.handleUltraEffectUse(targets, square);
            } catch (MoveException | ShootException | EffectException e) {
                WriterHelper.printErrorMessage("Errore nel settare l'ultra effetto");
            }
        }
        if(!game.isPlayerShooting())
            controlEndOfAction();
    }


}
