package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.model_to_view.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.server_thread.StartGameThread;
import it.polimi.ingsw.utils.WriterHelper;
import it.polimi.ingsw.view.RemoteView;


import java.util.*;

/**
 * Server class.
 * It represent the server that can be socket server or rmi server.
 */
public abstract class Server implements Runnable{

    private static final int WAITING_SECONDS = 10;
    private static final int MAX_PLAYERS_IN_GAME = 5;
    private static final String GAME_STARTING_MESSAGE = "Il server ha raggiunto un numero di giocatori sufficiente. La partita sta per iniziare.\n";
    private static final String NEW_PLAYER_CONNECTED_STRING_SUFFIX = " è stato aggiunto alla lista dei giocatori in attesa.";
    private static final String NEW_PLAYER_CONNECTED_STRING_PREFIX = "Il giocatore ";

    private final MainServer mainServer;

    /**
     * Constructor.
     * @param mainServer is the main server reference.
     */
    public Server(MainServer mainServer) {
        this.mainServer = mainServer;
    }

    /**
     * Connects a client to server.
     * The client now is a game clients.
     * Client is associated with a player.
     * @param clientConnection is the client connection.
     * @param playerName is the chosen player name.
     */
    public synchronized void rendezvous(ClientConnection clientConnection, String playerName){
        //Convert in uppercase
        String name = playerName.toUpperCase();
        //Control if player already exist and it need to reconnect
        if(mainServer.isDisconnectedPlayer(name)) {
            mainServer.reconnectPlayer(clientConnection, name);
            clientConnection.startPingThread(name);
            //If i have to reload the game
            if(!mainServer.isNewGame() && !mainServer.thereAreDisconnectedPlayers()) {
                mainServer.setNewGame(true);
                Controller controller = new Controller(mainServer.getGame());
                controller.setPath(mainServer.getSavePath());
                for (ClientConnection connection : mainServer.getGameClients().keySet()) {
                    RemoteView remoteView = new RemoteView(mainServer.getGameClients().get(connection), connection);
                    //Add controller as view observer
                    remoteView.getControllerUpdater().register(controller);
                    //Add view as model observer
                    mainServer.getGame().registerObserver(remoteView);
                    //Add view to user_interfaces list
                    mainServer.addView(remoteView);
                }
                //Restart game
                controller.restartGame();
            }
        }else{
            if(!mainServer.isGameStarted() || mainServer.getWaitingClientsNumber() >= MAX_PLAYERS_IN_GAME) {
                //Setting new player configuration
                Color color = mainServer.pickPlayerColor();
                Player player = new Player(name, color, false);
                //Adding client connection and his associated player to the waiting list
                getMainServer().moveConnectedToWaitingClient(clientConnection, player);
                String message = NEW_PLAYER_CONNECTED_STRING_PREFIX +player.getName()+ NEW_PLAYER_CONNECTED_STRING_SUFFIX;
                clientConnection.notifyLoginSuccess(LoginSuccess.INITIAL_LOGIN, name, "");
                WriterHelper.printWithTag("NEW_CONN", message);
                mainServer.getWaitingClients().keySet().forEach(c ->c.notifyUpdateWaitingRoom(playerName, null, mainServer.getWaitingPlayerNames(), false, 0));
                //If server has 3 players
                if(mainServer.getWaitingClientsNumber() == 3) {
                    WriterHelper.printlnOnConsole(GAME_STARTING_MESSAGE);
                    new StartGameThread(WAITING_SECONDS, mainServer).start();
                }
                //Start ping thread
                clientConnection.startPingThread(playerName);
            }else{
                //notify that game is already started
                clientConnection.notifyLoginError(LoginError.GAME_ALREADY_STARTED, name);
            }
        }
    }

    /**
     * Control if there is already a player with the chosen name.
     * @param name is the name to control.
     * @return true if the name already exists.
     */
    public synchronized boolean isNameAlreadyExisting(String name) {
        for(Player player: mainServer.getWaitingClients().values())
            if(player.getName().equals(name))
                return true;
        return false;
    }

    /**
     * Marks a connection as inactive.
     * @param clientConnection is the inactive connection.
     */
    public synchronized void setConnectionAsInactive(ClientConnection clientConnection) {
        clientConnection.setActive(false);
        mainServer.disconnectPlayer(clientConnection);
    }

    /**
     * Getter.
     * @return the main server.
     */
    public MainServer getMainServer() {
        return mainServer;
    }
}
