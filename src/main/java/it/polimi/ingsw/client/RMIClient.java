package it.polimi.ingsw.client;

import it.polimi.ingsw.client.user_interfaces.UserInterface;
import it.polimi.ingsw.server.IRMIClientConnection;
import it.polimi.ingsw.server.IRMIServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Class that create and run the RMI client.
 * It extends Client class.
 */
public class RMIClient extends Client {

    /**
     * Constructor.
     * @param serverPath the server ip address
     * @param serverPort the server rmi port
     * @param userInterface the user interface
     */
    public RMIClient(String serverPath, int serverPort, UserInterface userInterface) {
        super(serverPath, serverPort, userInterface);
    }

    /**
     * Starts the RMI Client.
     */
    @Override
    public void startClient() {
        String registryUrl = "rmi://"+getServerPath()+":"+getServerPort()+"/server";
        try {
            IRMIServer server = (IRMIServer) Naming.lookup(registryUrl);
            RMIServerConnection serverConnection = new RMIServerConnection(this, getUserInterface());
            IRMIClientConnection connection = server.connect(serverConnection);
            serverConnection.registerRMIClientConnection(connection);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            getUserInterface().showServerNotAvailableError();
        }
    }


}
