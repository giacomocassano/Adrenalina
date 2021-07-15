package it.polimi.ingsw.client;

import it.polimi.ingsw.client.user_interfaces.UserInterface;
import it.polimi.ingsw.events.client_to_server.ServerEvent;
import it.polimi.ingsw.events.model_to_view.EventFromModel;
import it.polimi.ingsw.server.IRMIClientConnection;
import it.polimi.ingsw.utils.WriterHelper;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class that handles the RMI connection with the server.
 * It extends ServerConnection class.
 */
public class RMIServerConnection extends ServerConnection implements IRMIServerConnection {

    private IRMIClientConnection connection;

    /**
     * Constructor.
     * @param client is the RMI Client
     * @param userInterface is the chosen user interface
     * @throws RemoteException when there are connection errors
     */
    public RMIServerConnection(RMIClient client, UserInterface userInterface) throws RemoteException {
        super(client, userInterface);
        UnicastRemoteObject.exportObject(this, 0);
    }

    /**
     * The remote method used to receive the events from server side
     * @param event is the event to receive
     * @throws RemoteException when there are connection errors
     */
    @Override
    public void receiveEventFromModel(EventFromModel event) throws RemoteException{
        getUserInterface().receiveEventFromModel(event);
    }

    /**
     * The method to send event to client side
     * @param event is the event to send
     */
    @Override
    public void asyncSendEvent(ServerEvent event) {
        new Thread(() -> {
            try {
                connection.receiveEventFromClient(event);
            } catch (RemoteException e) {
                WriterHelper.printErrorMessage("Errore nell'invio dell'evento al model.");
            }
        }).start();
    }

    /**
     * Register the RMI Client connection int the class.
     * @param connection is the RMI connection
     */
    public void registerRMIClientConnection(IRMIClientConnection connection) {
        this.connection = connection;
    }
}
