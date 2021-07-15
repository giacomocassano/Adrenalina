package it.polimi.ingsw.server;

import it.polimi.ingsw.client.IRMIServerConnection;
import it.polimi.ingsw.events.client_to_server.ServerEvent;
import it.polimi.ingsw.events.model_to_view.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class that represents the RMI client connection.
 * It implements her remote interface and handles rmi clients.
 * It receive and send events in the network.
 */
public class RMIClientConnection extends ClientConnection implements IRMIClientConnection {

    private transient IRMIServerConnection connection;
    private transient RMIServer rmiServer;

    /**
     * Constructor.
     * @param rmiServer is the rmi server.
     * @param connection is the server connection.
     * @throws RemoteException if there are connection issues.
     */
    public RMIClientConnection(RMIServer rmiServer, IRMIServerConnection connection) throws RemoteException {
        super(rmiServer);
        this.rmiServer = rmiServer;
        this.connection = connection;
        connection.receiveEventFromModel(new LoginRequest());
        UnicastRemoteObject.exportObject(this, 0);
    }

    /**
     * Sends event to client.
     * @param event is the event to send.
     */
    private void sendEvent(EventFromModel event) {
        try {
            connection.receiveEventFromModel(event);
        } catch (RemoteException e) {
            rmiServer.setConnectionAsInactive(this);
        }
    }

    /**
     * Sends event in a new thread.
     * @param event is the event to send.
     */
    @Override
    public void asyncSendEvent(EventFromModel event) {
        if(isActive())
            sendEvent(event);
        if(!event.isUpdateEvent())
            setLastEventSent(event);
    }

    /**
     * Method used to receive events from client.
     * @param event is the event received from client.
     * @throws RemoteException if there is a remote exception
     */
    @Override
    public void receiveEventFromClient(ServerEvent event) throws RemoteException{
        event.acceptServerVisitor(this);
    }

    /**
     * Getter.
     * @return the server connection.
     */
    public IRMIServerConnection getConnection() {
        return connection;
    }

}
