package it.polimi.ingsw.server;

import it.polimi.ingsw.client.IRMIServerConnection;
import it.polimi.ingsw.utils.WriterHelper;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * RMI server class.
 * It extends his remote interface.
 */
public class RMIServer extends Server implements IRMIServer{

    private static final int RMIPortNum = 12333;
    private static final String RMI_SERVER_ERROR_MESSAGE = "Errore nell'istanziare il server rmi.";
    private static final String RMI_SERVER_ACTIVATED_MESSAGE = "RMI server attivo...";
    private static final String RMI_ERROR_ALREADY_EXISTING_MESSAGE = "Esiste già un binding con il server rmi.";
    private IRMIServer stub;

    /**
     * Constructor.
     * @param mainServer is the main server.
     * @throws RemoteException is there are connection problems.
     */
    public RMIServer(MainServer mainServer) throws RemoteException{
        super(mainServer);
    }

    /**
     * Start the server and binds it in a server port.
     */
    @Override
    public void run() {
        try {
            stub = (IRMIServer) UnicastRemoteObject.exportObject(this, RMIPortNum);
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
            registry.bind("server", stub);
            WriterHelper.printWithTag("SERVER", RMI_SERVER_ACTIVATED_MESSAGE);
        } catch (RemoteException e) {
            WriterHelper.printErrorMessage(RMI_SERVER_ERROR_MESSAGE);
        } catch (AlreadyBoundException e) {
            WriterHelper.printErrorMessage(RMI_ERROR_ALREADY_EXISTING_MESSAGE);
        }

    }


    /**
     * Connect the client.
     * @param connection is the server connection established.
     * @return the new client connection established.
     * @throws RemoteException if there are connection issues.
     */
    @Override
    public IRMIClientConnection connect(IRMIServerConnection connection) throws RemoteException{
        RMIClientConnection conn = new RMIClientConnection(this, connection);
        getMainServer().addConnectedClient(conn);
        return conn;
    }

    /**
     * Disconnect client from server.
     * @param connection is the connection to disconnect.
     * @throws RemoteException if there is a remote exception
     */
    @Override
    public void disconnect(IRMIServerConnection connection) throws RemoteException {
        /*RMIClientConnection clientConnection = null;
        for(RMIClientConnection conn: activeConnections) {
            if(conn.getConnection() == connection)
                clientConnection = conn;
        }
        if(clientConnection != null){
            this.deregisterConnection(clientConnection);
            activeConnections.remove(clientConnection);
        }*/
    }


}
