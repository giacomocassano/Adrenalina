package it.polimi.ingsw.server.server_thread;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.model_to_view.WaitingRoomUpdate;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.MainServer;
import it.polimi.ingsw.utils.WriterHelper;
import it.polimi.ingsw.view.RemoteView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Thread that starts the game after players are connected to server.
 * This thread wait for tot seconds than if the number of players in the waiting room are from 3 to 5, it starts the match.
 * If player are less then 3 because someone disconnects, it ends without start the match.
 */
public class StartGameThread extends Thread {

    private final int seconds;
    private int timer;
    private final MainServer mainServer;

    private static final int GAME_SKULLS = 8;
    private static final int MAX_WAITING_CLIENTS = 3;
    private static final int MAX_PLAYER = 5;
    private static final int NUM_MAPS = 4;

    /**
     * Constructor.
     * @param seconds are the seconds to wait before start the match.
     * @param mainServer is the main server referece.
     */
    public StartGameThread(int seconds, MainServer mainServer) {
        this.seconds = seconds;
        this.mainServer = mainServer;
    }

    /**
     * Override of Thread class run method.
     * It wait for the indicate seconds than start the match it there are the right conditions.
     */
    @Override
    public void run() {
        WriterHelper.printOnConsole("La partita inizierà tra: " + seconds);
        timer = seconds;
        while(timer>=0 && mainServer.getWaitingClientsNumber() < MAX_PLAYER && mainServer.getWaitingClientsNumber() >= MAX_WAITING_CLIENTS) {
            //If a client log out and the waiting clients are less of 3, the timer restarts
            if(mainServer.getWaitingClientsNumber() < MAX_WAITING_CLIENTS)
                timer = seconds;
            WriterHelper.printOnConsole("\b\b");
            if(timer>9)
                WriterHelper.printOnConsole(""+timer);
            else
                WriterHelper.printOnConsole(" "+timer);
            timer--;
            //Sleep one second
            try {
                if(timer >= 0)
                    notifyWaitingRoom();
                sleep(1000);
            } catch (InterruptedException e) {
                WriterHelper.printErrorMessage("Errore nello sleep del thread!");
            }
        }
        WriterHelper.printlnOnConsole("");

        if(mainServer.getWaitingClientsNumber() > 2) {
            //Getting waiting clients and active connections lists
            Map<ClientConnection, Player> waitingClients = mainServer.getWaitingClients();
            //Getting players
            List<Player> players = new ArrayList<>(waitingClients.values());
            //Create model and controller
            Game game = new Game(players, GAME_SKULLS, new Random().nextInt(NUM_MAPS) + 1);
            game.startDecksMapsFromXML();
            Controller controller = new Controller(game);
            controller.setPath(mainServer.getSavePath());
            //For each player create a remote view
            for (ClientConnection connection : waitingClients.keySet()) {
                RemoteView remoteView = new RemoteView(waitingClients.get(connection), connection);
                //Add controller as view observer
                remoteView.getControllerUpdater().register(controller);
                //Add view as model observer
                game.registerObserver(remoteView);
                //Add view to user_interfaces list
                mainServer.addView(remoteView);
            }
            //Clear the waiting client list and add all to the game clients
            mainServer.setupGameClients();
            //Server don't have to accept other client
            mainServer.setGameStarted(true);
            //Start the game
            mainServer.setGame(game);
            controller.startGame();
        }else{
            WriterHelper.printErrorMessage("Partita abortita. Il numero di giocatori non è sufficiente.");
        }

    }

    /**
     * Notifies the waiting room update.
     */
    private void notifyWaitingRoom() {
        for(ClientConnection c: mainServer.getWaitingClients().keySet())
            c.asyncSendEvent(new WaitingRoomUpdate(null, null, mainServer.getWaitingPlayerNames(), true, timer));
    }
}
