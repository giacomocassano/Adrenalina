package it.polimi.ingsw.client.client_thread;

import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.client.user_interfaces.UserInterface;
import it.polimi.ingsw.utils.WriterHelper;

/**
 *
 * Class that controls the connection with the server.
 * The server is connected if the ping event arrives in a time lower than RESPONSE_SECONDS
 *
 */
public class ClientPingThread implements Runnable {

    private static final String PING_THREAD_ERROR_MESSAGE = "Errore nello sleep del ClientPingThread.\n.";
    private static final int ONE_SECOND = 1000;
    private boolean isConnected;
    private final ServerConnection serverConnection;
    private final static int RESPONSE_SECONDS = 6;
    private final UserInterface userInterface;

    /**
     * Constructor.
     * @param serverConnection is the connection with the server
     * @param userInterface is the chosen user interface
     */
    public ClientPingThread(ServerConnection serverConnection, UserInterface userInterface) {
        this.isConnected = true;
        this.userInterface = userInterface;
        this.serverConnection = serverConnection;
    }

    /**
     * Override of run method of Runnable class.
     * It checks the connection every REPONSE_SECONDS seconds.
     */
    @Override
    public void run() {
        while(isConnected) {
            this.isConnected = false;
            try {
                Thread.sleep((long) RESPONSE_SECONDS * ONE_SECOND);
            } catch (InterruptedException e) {
                WriterHelper.printErrorMessage(PING_THREAD_ERROR_MESSAGE);
            }
        }
        userInterface.showServerNotAvailableError();
    }

    /**
     * Set connection as active
     * @param connected is true if connection is active
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
