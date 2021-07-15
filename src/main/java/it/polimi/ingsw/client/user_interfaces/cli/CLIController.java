package it.polimi.ingsw.client.user_interfaces.cli;

import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.client.user_interfaces.UserInterface;
import it.polimi.ingsw.events.client_to_server.to_server.SelectedName;
import it.polimi.ingsw.events.model_to_view.*;
import it.polimi.ingsw.events.client_to_server.to_controller.*;
import it.polimi.ingsw.events.visitors.ViewVisitor;
import it.polimi.ingsw.exceptions.TimeExceededException;
import it.polimi.ingsw.utils.ANSIColors;
import it.polimi.ingsw.utils.Config;
import it.polimi.ingsw.utils.WriterHelper;

import java.util.List;

/**
 * Class to handle events from server in COMMAND LINE INTERFACE
 */
public class CLIController extends UserInterface implements ViewVisitor {

    private CommandLineInterface cli;
    private GameBoard gameBoard;
    private static final boolean HIDE_CLI = false;
    private String myName;
    private static final String MAX_PLAYERS_MESSAGE="Mi dispiace! La partita ha già raggiunto un numero di giocatori sufficiente!";
    private static final String WINNING_MESSAGE="Congratulazioni! Hai vinto!\n\n";
    private static final String LOSING_MESSAGE="Mi dispiace, hai perso!\n\n";
    private static final String PARITY_MESSAGE="Partita patta. I seguenti giocatori hanno pareggiato:";

    /**
     * Constructor
     * @param config is a configuration obj. More at Config.class
     */
    public CLIController(Config config) {
        super(config);
        this.cli = new CommandLineInterface(getServerConnection());
        myName = "";
        this.gameBoard = new GameBoard();
    }

    /**
     * Method that visit an event recived from model. Override from User Interface.
     * @param event is event that is recived
     */
    @Override
    public void receiveEventFromModel(EventFromModel event) {
        //Visitor acceptControllerVisitor the event and, in the method, the event call the corresponding visitor method
        event.acceptViewVisitor(this);
    }

    /**
     * Method to print cli on window.
     */
    private void printCli() {
        if (!HIDE_CLI) {
            gameBoard.startBoard(getGameRepresentation());
            gameBoard.plot(getGameRepresentation().getOtherPlayerRapresentation(), getGameRepresentation().getMyPlayerRepresentation(), getGameRepresentation().getMapRepresentation().getMapType(), getGameRepresentation().getGameBoardRepresentation().getKillshotTrackRep());
        }
    }

    /**
     * Server connection setter
     * @param serverConnection is a server connection
     */
    @Override
    public void setServerConnection(ServerConnection serverConnection) {
        super.setServerConnection(serverConnection);
        cli.setConnection(serverConnection);
    }

    /**
     * Prints an error message if server is not available
     */
    @Override
    public void showServerNotAvailableError() {
        WriterHelper.printServerNotAvailableErrorMessage();
        System.exit(1);
    }

    /* Visit methods */

    /**
     * Visit method of Login Request event.
     * @param event is a loginRequest event.
     */
    @Override
    public synchronized void visit(LoginRequest event) {
        new Thread(() -> {
            String name = cli.askName(true);
            getServerConnection().asyncSendEvent(new SelectedName(name));
        }).start();
    }

    /**
     * Visit method of LoginSuccess event
     * @param event is a login success event.
     */
    @Override
    public void visit(LoginSuccess event) {
        getServerConnection().startPingThread();
        new Thread(() -> {
            cli.notifyNameSuccess(event.getType(), event.getNameConfirmed(), event.getWelcomeMessage());
        }).start();
    }

    /**
     * Visit method of LoginError event
     * @param event is a login error event
     */
    @Override
    public void visit(LoginError event) {
        new Thread(() -> {
            if (event.getType() == LoginError.NAME_ALREADY_EXISTING) {
                String name = cli.askName(false);
                getServerConnection().asyncSendEvent(new SelectedName(name));
            }else{
                WriterHelper.printlnColored(ANSIColors.RED, MAX_PLAYERS_MESSAGE);
                System.exit(0); //Exit successful
            }
        }).start();
    }

    /**
     * Visit method of Spawn Request event.
     * @param request is a Spawn Request message sent from server
     */
    @Override
    public void visit(SpawnRequest request) {
        //Print the board
        printCli();
        //Ask born
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                FirstSpawnResponse response = null;
                try {
                    response = cli.askBorn(request.getPlayer(), request.getEmpowers());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }else
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, request.getPlayer() + " sta per nascere.");
    }


    /**
     * Visit method of EmpowerRequest
     * @param request is an empower request sent by server
     */
    @Override
    public void visit(EmpowerRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                EmpowerResponse response = null;
                try {
                    response = cli.selectEmpower(request.getPlayer(), request.getValidEmpowers());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }
    }

    /**
     * Visit method of MoveRequest
     * @param request is a Move request sent by server
     */
    @Override
    public void visit(MoveRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                try {
                    MoveResponse response = cli.selectMove(request.isFinalFrenzy(), request.getPlayer(), request.getValidMoves(), request.getActionNumber(), request.getMoveNumber());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }
    }

    /**
     * Visit method of SpawnRequest
     * @param request is a spawn request sent by server
     */
    @Override
    public void visit(RespawnRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                try {
                    RespawnResponse response = cli.selectRespawn(request.getPlayer(), request.getEmpowers());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }
    }

    /**
     * Visit method of SquareMoveRequest
     * @param request is a SquareMove request sent by server
     */
    @Override
    public void visit(SquareMoveRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                try {
                    SquareToMoveResponse response = cli.selectSquare(request.getPositions(), getGameRepresentation().getMyPlayerRepresentation().getName(),
                            getGameRepresentation().getMyPlayerRepresentation().getPosition());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }else
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, request.getPlayer() + " sta scegliendo dove muoversi.");
    }

    /**
     * Visit method of WeaponToGrabRequest
     * @param request is an weapon to grab request sent by server
     */
    @Override
    public void visit(WeaponToGrabRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                try {
                    WeaponToGrabResponse response = cli.selectWeapontoGrab(getGameRepresentation().getMyPlayerRepresentation().getName(), request.getWeaponsToGrab(),
                            request.getWeaponsToDrop(), request.getPayingEmpowers());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }else
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, request.getPlayer() + " sta comprando un'arma.");
    }

    /**
     * Visit method of ShootWeaponRequest
     * @param request is a ShootWeapon request sent by server
     */
    @Override
    public void visit(ShootWeaponRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                try {
                    WeaponToShootResponse response = cli.selectWeaponToShoot(getGameRepresentation().getMyPlayerRepresentation().getName(), request.getWeapons());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }else
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, request.getPlayer() + " sta per scegliendo l'arma con cui sparare.");
    }

    /**
     * Visit method of BasicEffectRequest
     * @param request is a basic effect request sent by server
     */
    @Override
    public void visit(BasicEffectRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                try {
                    BasicEffectResponse response = cli.selectWeaponEffect(getGameRepresentation().getMyPlayerRepresentation().getName(),
                            request.getBasicEffects(), request.getPayingEmpowers());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }else
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, request.getPlayer() + " sta scegliendo l'effetto base.");
    }

    /**
     * Visit method of BasicEffectUse Request
     * @param request is a basic effect use request sent by server
     */
    @Override
    public void visit(BasicEffectUseRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                try {
                    BasicEffectUseResponse response = cli.selectBasicEffectUse(getGameRepresentation().getMyPlayerRepresentation().getName(),
                            request.getPlayerNames(), request.getSquarePositions(), request.getMaxPlayers(), request.getMaxSquares(), request.getRecoilPositions());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }else
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, request.getPlayer() + " sta per usare l'effetto base.");
    }

    /**
     * Visit method of UltraEffectRequest
     * @param request is an ultra effect request sent by server
     */
    @Override
    public void visit(UltraEffectRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                try {
                    UltraEffectResponse response = cli.selectWeaponUltraEffect(getGameRepresentation().getMyPlayerRepresentation().getName(), request.getUltra());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }else
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, request.getPlayer() + " sta scegliendo l'effetto aggiuntivo.");
    }

    /**
     * Visit method of UltraEffectUseRequest
     * @param request is an ultra effect use request sent by server
     */
    @Override
    public void visit(UltraEffectUseRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                try {
                    UltraEffectUseResponse response = cli.selectUltraEffectUse(getGameRepresentation().getMyPlayerRepresentation().getName(),
                            request.getPlayerNames(), request.getSquarePositions(), request.getMaxPlayers(), request.getMaxSquares());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }else
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, request.getPlayer() + " sta settando l'effetto aggiuntivo.");
    }


    /**
     * Visit method of WeaponsToReload
     * @param request is a weapons to reload request sent by server
     */
    @Override
    public void visit(WeaponsToReloadRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                try {
                    WeaponsToReloadResponse response = cli.selectWeaponToReload(request.getWeaponsToReload(), getGameRepresentation().getMyPlayerRepresentation().getName(),
                            request.getAmmoCubes(), request.getPayingEmpowers());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }else
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, request.getPlayer() + " sta per ricaricare le sue armi.");
    }

    /**
     * Visit method of a Time Exceeded Notification
     * @param event is a Time exceeded notification
     */
    @Override
    public void visit(TimeExceededNotification event) {
        cli.stopRead();
    }

    /**
     * Visit method of messageNotification event.
     * @param event is a messageNotification event used to give some infos to client.
     */

    @Override
    public void visit(MessageNotification event) {
        cli.showMessage(event.getMessage());
    }

    /**
     * Visit-method of GameRepUpdate
     * @param event is a Game Representation update event
     * More infos in package @events, same class.
     */
    @Override
    public void visit(GameRepUpdate event) {
        getGameRepresentation().setMapRepresentation(event.getMapRep());
        getGameRepresentation().setMyPlayerRepresentation(event.getMyPlayerRep());
        getGameRepresentation().setOtherPlayersRepresentations(event.getOtherPlayersReps());
        getGameRepresentation().setGameBoardRepresentation(event.getGameBoardRepresentation());
        this.myName = getGameRepresentation().getMyPlayerRepresentation().getName();
    }

    /**
     * Visit-method of EmpowerUseRequest
     * More at EmpowerUseRequest in the package @events.
     * @param request is an empower use request
     */
    @Override
    public void visit(EmpowerUseRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                EmpowerUseResponse response = null;
                try {
                    response = cli.selectEmpowerUse(request.getPlayer(), request.getEmpower(), request.getPossibleTargets(), request.getPossiblePositions(), request.getPayingEmpowers(), request.getAmmoCubes(), request.isPayable());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }
    }

    /**
     * WaitingRoom update visit method.
     * @param update is a WaitingRoomUpdate, more at the same class in @events package
     */

    @Override
    public void visit(WaitingRoomUpdate update) {
        cli.printWaitingRoom(update.getConnectedPlayer(), update.getDisconnectedPlayer(), update.getWaitingPlayers(), update.isStarting(), update.getTimer());
    }


    /**
     * Visit of ShooterMovementRequest. More at ShooterMovementRequest class in events.
     * @param request is a shooter mov. request
     */
    @Override
    public void visit(ShooterMovementRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                ShooterMovementResponse response = null;
                try {
                    response = cli.selectMovement(request.getPlayer(), request.getPossiblePositions(), request.getPayingEmpowers(), request.getMovement());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }
    }

    /**
     * Visit-method of GrabAmmo request
     * More at GrabAmmoRequest in the package @events.
     * @param request is a grab ammo request
     */
    @Override
    public void visit(GrabAmmoRequest request) {
        printCli();
        if (request.getPlayer().equals(myName)) {
            new Thread(() -> {
                GrabAmmoResponse response = null;
                try {
                    response = cli.acceptAmmo(request.getPlayer(), request.getCubes(), request.isHasEmpower());
                    if (response != null) {
                        getServerConnection().asyncSendEvent(response);
                    }
                } catch (TimeExceededException e) {
                    WriterHelper.printlnOnConsole("Tempo scaduto.");
                }
            }).start();
        }else
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, request.getPlayer() + " sta per raccogliere una carta munizione.");
    }

    /**
     * Visit method of GameOverNotification event.
     * @param notification is a GameOverNotification, more at GameOverNotification.
     */
    @Override
    public void visit(GameOverNotification notification) {
        String winner = notification.getWinner();
        List<String> tiePlayers = notification.getTiePlayers();
        WriterHelper.printWhiteRows(50);
        if(winner != null) {
            WriterHelper.printCup(winner);
            if(winner.equals(myName))
                WriterHelper.printlnColored(ANSIColors.YELLOW_BOLD,WINNING_MESSAGE );
            else
                WriterHelper.printlnColored(ANSIColors.YELLOW_BOLD,LOSING_MESSAGE);
        }else if(tiePlayers != null && !tiePlayers.isEmpty()) {
            WriterHelper.printlnColored(ANSIColors.YELLOW_BOLD,PARITY_MESSAGE);
            for(String p: tiePlayers)
                WriterHelper.printColored(ANSIColors.YELLOW_BOLD, p);
            WriterHelper.printlnOnConsole("\n\n");
        }

        WriterHelper.printRanking(notification.getRanking());
        System.exit(0);
    }

    /**
     * Visit method of DisconnectionNotification. More at @DisconnectionNotification
     * @param notification is the DisconnectionNotification message sent by server
     */
    @Override
    public void visit(DisconnectionNotification notification) {
        WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, notification.getDisconnectedPlayer() + " si è disconnesso dalla partita.");
    }

    /**
     * Visit method of ReconnectionNotification. More at @ReconnectionNotification
     * @param notification is the ReconnectionNotification message sent by server
     */
    @Override
    public void visit(ReconnectionNotification notification) {
        if(myName != null && !notification.getReconnectedPlayer().equals(myName))
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, notification.getReconnectedPlayer() + " si è riconnesso alla partita.");
    }

    /**
     * Visit of Ping client event. More infos at PingClient in events
     * @param event is the ping client event.
     */

    @Override
    public void visit(PingClient event) {
        getServerConnection().pingReceived();
        new Thread(() -> getServerConnection().pingServer()).start();
    }
}

