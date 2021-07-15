package it.polimi.ingsw.server;

import it.polimi.ingsw.events.client_to_server.ServerEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface used to declare remote methods.
 * It will be implemented by RMIClientConnection.
 */
public interface IRMIClientConnection extends Remote {
    /**
     * Method used to recieve event from Client.
     * @param event is the event received from client.
     * @throws RemoteException if there are connection errors.
     */
    void receiveEventFromClient(ServerEvent event) throws RemoteException;
}
