package it.polimi.ingsw.server;

import it.polimi.ingsw.client.IRMIServerConnection;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface used to declare remote server methods.
 * It will be implemented by RMIServer.
 */
public interface IRMIServer extends Remote {
    /**
     * Connects the client to the server.
     * @param connection is the server connection established.
     * @return the established client connection.
     * @throws RemoteException if there are connection issues.
     */
    IRMIClientConnection connect(IRMIServerConnection connection) throws RemoteException;

    /**
     * Disconnect connection.
     * @param connection is the connection to disconnect.
     * @throws RemoteException if there are connection issues.
     */
    void disconnect(IRMIServerConnection connection) throws RemoteException;
}
