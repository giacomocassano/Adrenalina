package it.polimi.ingsw.client;

import it.polimi.ingsw.client.user_interfaces.UserInterface;

/**
 *  Abstract class that represent the client. It will be extended by SocketClient and RMIClient.
 */
public abstract class Client {

    private final String serverPath;
    private final int serverPort;
    private final UserInterface userInterface;

    /**
     * Constructor.
     * @param serverPath the server ip address
     * @param serverPort the server port
     * @param userInterface the user interface
     */
    public Client(String serverPath, int serverPort, UserInterface userInterface) {
        this.userInterface = userInterface;
        this.serverPath = serverPath;
        this.serverPort = serverPort;
    }

    /**
     * Starts the client. RMI Client and Socket Client override this method.
     */
    public abstract void startClient();

    /**
     *
     * @return the user interface
     */
    public UserInterface getUserInterface() {
        return userInterface;
    }

    /**
     *
     * @return the server ip address
     */
    public String getServerPath() {
        return serverPath;
    }

    /**
     *
     * @return the server port
     */
    public int getServerPort() {
        return serverPort;
    }

}
