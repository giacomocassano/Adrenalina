package it.polimi.ingsw.client.user_interfaces;

import it.polimi.ingsw.client.RMIClient;
import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.client.SocketClient;
import it.polimi.ingsw.client.user_interfaces.model_representation.GameRepresentation;
import it.polimi.ingsw.events.model_to_view.EventFromModel;
import it.polimi.ingsw.utils.Config;


public abstract class UserInterface {

    private GameRepresentation gameRepresentation;

    private Config config;

    private ServerConnection serverConnection;

    public UserInterface(Config config) {
        this.config = config;
        gameRepresentation = new GameRepresentation();
    }

    public GameRepresentation getGameRepresentation() {
        return gameRepresentation;
    }

    public abstract void receiveEventFromModel(EventFromModel event);

    public void setServerConnection(ServerConnection connection) {
        this.serverConnection = connection;
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }

    public void startRmiClient() {
        RMIClient rmiClient = new RMIClient(config.getServerIp(), config.getRmiPort(), this);
        rmiClient.startClient();
    }

    public void startSocketClient() {
        SocketClient socketClient = new SocketClient(config.getServerIp(), config.getSocPort(), this);
        socketClient.startClient();
    }

    public abstract void showServerNotAvailableError();
}
