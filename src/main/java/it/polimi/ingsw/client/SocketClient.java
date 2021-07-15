package it.polimi.ingsw.client;

import it.polimi.ingsw.client.user_interfaces.UserInterface;
import it.polimi.ingsw.utils.WriterHelper;

import java.io.*;
import java.net.Socket;

/**
 * The class that create and run the socket client.
 * It extends Client class.
 */
public class SocketClient extends Client {

    private Socket socket;

    /**
     * Constructor.
     * @param serverPath the server ip address
     * @param serverPort the server socket port
     * @param userInterface the chosen user interface
     */
    public SocketClient(String serverPath, int serverPort, UserInterface userInterface) {
        super(serverPath, serverPort, userInterface);
    }

    /**
     * Starts the client.
     */
    @Override
    public void startClient() {
        try {
            socket = new Socket(getServerPath(), getServerPort());
            SocketServerConnection socketServerConnection = new SocketServerConnection(this, socket, getUserInterface());
            new Thread(socketServerConnection).start();
        } catch (IOException e) {
            getUserInterface().showServerNotAvailableError();
        }
    }
}
