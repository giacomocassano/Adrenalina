package it.polimi.ingsw.client;

import it.polimi.ingsw.events.model_to_view.EventFromModel;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface used to declare the remote method.
 * It will be extended by RMIServerConnection.
 */
public interface IRMIServerConnection extends Remote {
    /**
     * Method used to receive events from Server.
     * @param event the event to send
     * @throws RemoteException when there are connection errors
     */
    void receiveEventFromModel(EventFromModel event) throws RemoteException;
}
