package it.polimi.ingsw.server.server_thread;

import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utils.WriterHelper;

/**
 * Class that controls the client connections.
 * It sends a ping to client each PING_INTERVAL_TIME milliseconds.
 */
public class PingThread implements Runnable {

    private final ClientConnection clientConnection;
    private boolean active;
    private final static int PING_INTERVAL_TIME = 1000;

    /**
     * Constructor.
     * @param clientConnection is the client connection.
     */
    public PingThread(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        this.active = true;
    }

    /**
     * Override of run method of Runnable interface.
     * It's the method taht sends ping to client connection.
     */
    @Override
    public void run() {
        while(active)  {
            clientConnection.pingClient();
            try {
                Thread.sleep(PING_INTERVAL_TIME);
            } catch (InterruptedException e) {
                WriterHelper.printErrorMessage("Errore nello sleep del thread del ping.");
            }
        }
    }

    /**
     * Setter.
     * @param active is the param to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
