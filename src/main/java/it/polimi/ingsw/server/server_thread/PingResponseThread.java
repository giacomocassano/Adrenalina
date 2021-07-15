package it.polimi.ingsw.server.server_thread;

import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utils.WriterHelper;

/**
 * Class that controls the connection with the client.
 * The client is connected if the a ping event arrives in a time lower than RESPONSE_TIME seconds.
 */
public class PingResponseThread implements Runnable {

    private boolean isConnected;
    private ClientConnection clientConnection;
    private final static int RESPONSE_TIME = 6;
    private final String name;

    /**
     * Constructor.
     * @param clientConnection is the connection with client
     * @param name is the name of the player
     */
    public PingResponseThread(ClientConnection clientConnection, String name){
        this.isConnected = true;
        this.clientConnection = clientConnection;
        this.name = name;
    }

    /**
     * Override of run method of Runnable class.
     * It checks the connection every REPONSE_TIME seconds.
     */
    @Override
    public void run() {
        while(isConnected) {
            isConnected = false;
            try {
                Thread.sleep(RESPONSE_TIME * 1000);
            } catch (InterruptedException e) {
                WriterHelper.printErrorMessage("Errore nello sleep di PingResponseThread\n");
            }
        }
        WriterHelper.printErrorMessage("Non ho ricevuto il ping dal client, disconnetto "+name+".\n");
        clientConnection.disconnectConnectionFromServer();
    }

    /**
     * Set the connection as active
     * @param connected is true if the connection is active
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
