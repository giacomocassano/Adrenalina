package it.polimi.ingsw.server;

import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.events.model_to_view.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.AmmunitionCard;
import it.polimi.ingsw.model.cards.EmpowerCard;
import it.polimi.ingsw.model.cards.Stack;
import it.polimi.ingsw.model.cards.WeaponCard;
import it.polimi.ingsw.utils.ANSIColors;
import it.polimi.ingsw.utils.GsonHelper;
import it.polimi.ingsw.utils.WriterHelper;
import it.polimi.ingsw.utils.XMLHelper;
import it.polimi.ingsw.view.RemoteView;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.Color.*;

/**
 * Main server of the application.
 * It contains socket server and rmi server.
 */
public class MainServer {

    private static final String CONFIGURATION_XML_ERROR_MESSAGE = "Non sono riuscito a ricaricare il gioco. Mi dispiace, prova a far partire una " +
            "nuova partita senza il comando -load.";
    private static final String RECONNECTED_MESSAGE = " riconnesso con successo\n";
    private static final String DISCONNECTED_MESSAGE = " disconnesso.";
    private final SocketServer socketServer;
    private final RMIServer rmiServer;

    private List<ClientConnection> connectedClients;
    private Map<ClientConnection, Player> waitingClients;
    private Map<ClientConnection, Player> gameClients;
    private List<RemoteView> views;
    private Map<Player, ClientConnection> disconnectedPlayers;
    private List<Color> playerColors;
    private boolean newGame;
    private Game game;
    private boolean isGameStarted;
    private final String savePath;

    /**
     * Constructor.
     * @throws IOException if he can't instance the servers.
     * @param savePath is the path of the file to save
     */

    public MainServer(String savePath) throws IOException {
        this.savePath = savePath;
        //Initialize lists and maps
        connectedClients = new ArrayList<>();
        waitingClients = new LinkedHashMap<>();
        gameClients = new LinkedHashMap<>();
        disconnectedPlayers = new LinkedHashMap<>();
        views = new ArrayList<>();
        playerColors = new ArrayList<>(Arrays.asList(YELLOW, GREEN, GREY, PURPLE, BLUE));
        this.isGameStarted = false;
        //Create server
        this.socketServer = new SocketServer(this);
        this.rmiServer = new RMIServer(this);
        newGame = true;
    }

    /**
     * Starts both servers.
     */
    public void startServer() {
        new Thread(socketServer).start();
        new Thread(rmiServer).start();
    }

    /**
     * Method called to indicate the disconnected player of the match that will be reloaded.
     * It's a PERSISTENCE FUNCTIONALITY method.
     * @param players are disconnected players
     */
    public void addDisconnectedPlayers(List<Player> players) {
        for(Player player: players)
            disconnectedPlayers.put(player, null);
    }

    /**
     * Load the existing game for PERSISTENCE FUNCTIONALITY.
     * It loads the game from XML file.
     * @param path is the path of the file to load
     */
    public void loadExistingGame(String path) {
        this.isGameStarted = true;
        this.newGame = false;
        it.polimi.ingsw.model.cards.Stack<WeaponCard> weaponCardStack = new it.polimi.ingsw.model.cards.Stack<>(XMLHelper.forgeWeaponDeck());
        it.polimi.ingsw.model.cards.Stack<EmpowerCard> empowerCardStack = new it.polimi.ingsw.model.cards.Stack<>(XMLHelper.forgeEmpowerDeck());
        it.polimi.ingsw.model.cards.Stack<AmmunitionCard> ammunitionCardStack = new Stack<>(XMLHelper.forgeAmmoDeck());
        List<Player> players;
        try {
            JsonReader reader = new JsonReader(new FileReader(path));
            reader.beginObject();
            it.polimi.ingsw.model.Map map=GsonHelper.readMap(reader, weaponCardStack,empowerCardStack,ammunitionCardStack);
            players= GsonHelper.readPlayers(reader,map,empowerCardStack,weaponCardStack,ammunitionCardStack);
            GsonHelper.readPlayersDamagesAndMarks(reader,players);
            Game game=GsonHelper.readGameInfosAndStartGame(reader,players,map);
            game.setKillshotTrack(GsonHelper.readKillShotTrack(reader,players,game));
            reader.endObject();
            reader.close();
            this.game=game;
            game.reloadStacks(ammunitionCardStack,empowerCardStack,weaponCardStack);
        }catch (IOException e){
            WriterHelper.printErrorMessage(CONFIGURATION_XML_ERROR_MESSAGE);
        }
    }

    /**
     * Add a client connection.
     * @param clientConnection is the added connection.
     */
    public synchronized void addConnectedClient(ClientConnection clientConnection) {
        connectedClients.add(clientConnection);
    }

    /**
     * Marks a client connection as a waiting connection.
     * Waiting connections are the connections that are waiting for the start of the game.
     * @param clientConnection is the connection to move.
     * @param player is the associated player of the connection.
     */
    public synchronized void moveConnectedToWaitingClient(ClientConnection clientConnection, Player player) {
        connectedClients.remove(clientConnection);
        waitingClients.put(clientConnection, player);
    }

    /**
     * Setup the game client.
     * It moves the waiting clients to the game clients list.
     */
    public synchronized void setupGameClients() {
        gameClients = new LinkedHashMap<>(waitingClients);
        waitingClients.clear();
    }

    /**
     * Getter of the waiting players names.
     * @return the waiting players names.
     */
    public List<String> getWaitingPlayerNames() {
        return this.waitingClients.values().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    /**
     * Getter of the waiting client number.
     * @return the waiting client number.
     */
    public synchronized int getWaitingClientsNumber() {
        return waitingClients.size();
    }

    /**
     * Getter of waiting client.
     * @return the waiting client list.
     */
    public Map<ClientConnection, Player> getWaitingClients() {
        return waitingClients;
    }

    /**
     * Getter of connected clients.
     * @return the connected client list.
     */
    public List<ClientConnection> getConnectedClients() {
        return connectedClients;
    }

    /**
     * Disconnects an inactive player.
     * @param connection is the inactive connection.
     */
    public void disconnectPlayer(ClientConnection connection) {
        Player discPlayer = null;
        if(isGameStarted) {
            discPlayer = gameClients.get(connection);
            if (discPlayer != null) {
                discPlayer.setConnected(false);
                gameClients.remove(connection);
                disconnectedPlayers.put(discPlayer, connection);
                for(ClientConnection client: gameClients.keySet())
                    client.notifyDisconnection(discPlayer.getName());
            }
        }else{
            discPlayer = waitingClients.get(connection);
            if(discPlayer != null) {
                waitingClients.remove(connection);
                for (ClientConnection client : waitingClients.keySet())
                    client.notifyUpdateWaitingRoom(null, discPlayer.getName(), getWaitingPlayerNames(), false, 0);
            }
        }
        if(discPlayer != null)
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, discPlayer.getName() + DISCONNECTED_MESSAGE);
    }

    /**
     * Reconnect a disconnect player.
     * @param connection is the new connection of the player.
     * @param playerName is the reconnected player name.
     */
    public void reconnectPlayer(ClientConnection connection, String playerName) {
        Player player = null;
        for(Player p: disconnectedPlayers.keySet())
            if(p.getName().equals(playerName))
                player = p;
        if(player != null) {
            player.setConnected(true);
            gameClients.put(connection, player);
            for(RemoteView view: views)
                if(view.getPlayer() == player) {
                    view.setClientConnection(connection);
                    view.notifyGameRepContext(view.getPlayer(), game, true);
                }
            //Notify the reconnection
            List<String> playerNames = gameClients.values().stream().map(Player::getName).collect(Collectors.toList());
            for(ClientConnection client: gameClients.keySet())
                client.notifyReconnection(player, playerNames);
            //Notify login success to the reconnected client
            connection.notifyLoginSuccess(LoginSuccess.RECONNECT_SUCCESS, playerName, "");
            //Send the last event sent
            if(disconnectedPlayers.get(player) != null && disconnectedPlayers.get(player).getLastEventSent() != null)
                connection.asyncSendEvent(disconnectedPlayers.get(player).getLastEventSent());
            WriterHelper.printWithTagColored(ANSIColors.BLUE_BOLD, "REC", playerName + RECONNECTED_MESSAGE);
            disconnectedPlayers.remove(player);
        }
    }

    /**
     * Getter of game clients.
     * @return game clients
     */
    public Map<ClientConnection, Player> getGameClients() {
        return gameClients;
    }

    /**
     * Pick a new player color.
     * @return the picked player color.
     */
    public Color pickPlayerColor() {
        if(playerColors.isEmpty())
            return null;
        else
            return playerColors.remove(new Random().nextInt(playerColors.size()));
    }

    /**
     * Control if a player is a disconnected one.
     * @param playerName the player to control.
     * @return true if the player is disconnected.
     */
    public boolean isDisconnectedPlayer(String playerName) {
        List<String> disconnectedPlayerNames = disconnectedPlayers.keySet().stream().map(Player::getName).collect(Collectors.toList());
        return disconnectedPlayerNames.contains(playerName);
    }

    /**
     * Add a view to views list.
     * @param view is the view to add.
     */
    public void addView(RemoteView view) {
        this.views.add(view);
    }

    /**
     * Getter of game started attribute.
     * @return true if the game is started.
     */
    public boolean isGameStarted() {
        return isGameStarted;
    }

    /**
     * Setter of game started attribute.
     * @param gameStarted is the attribute to set.
     */
    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    /**
     * Game setter
     * @param game is the game to set.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Getter.
     * @return true if the game is not loaded from file.
     */
    public boolean isNewGame() {
        return newGame;
    }

    /**
     * Getter.
     * @return the actual game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Control if there are disconnected player.
     * @return true is the disconnected players list is not empty.
     */
    public boolean thereAreDisconnectedPlayers() {
        return !disconnectedPlayers.isEmpty();
    }

    /**
     * Getter.
     * @return the save path.
     */
    public String getSavePath() {
        return savePath;
    }

    /**
     * Setter.
     * @param newGame is true if server have to reload game.
     */
    public void setNewGame(boolean newGame) {
        this.newGame = newGame;
    }
}
