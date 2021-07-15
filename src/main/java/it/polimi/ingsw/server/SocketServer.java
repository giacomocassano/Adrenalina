package it.polimi.ingsw.server;


import it.polimi.ingsw.utils.WriterHelper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Socket server.
 * It connects socket client connections.
 */
public class SocketServer extends Server{

    private static final int PORT = 12345;
    private static final int MAX_PLAYERS = 5;
    private static final String SOCKET_SERVER_ACTIVATED_MESSAGE = "Socket server attivo...";
    private ServerSocket serverSocket;

    /**
     * Constructor.
     * @param mainServer is the main server reference.
     * @throws IOException if there are connection issues.
     */
    public SocketServer(MainServer mainServer) throws IOException {
        super(mainServer);
        serverSocket = new ServerSocket(PORT);
    }

    /**
     * Override of run method of Runnable class.
     * Create the socket server and wait for client connection.
     */
    @Override
    public void run() {
        WriterHelper.printWithTag("SERVER", SOCKET_SERVER_ACTIVATED_MESSAGE);
        while(getMainServer().getWaitingClientsNumber() < MAX_PLAYERS) {
            try{
                Socket clientSocket = serverSocket.accept();
                SocketClientConnection socketClientConnection = new SocketClientConnection(this, clientSocket);
                getMainServer().addConnectedClient(socketClientConnection);
                new Thread(socketClientConnection).start();
            }catch (IOException ex) {
                WriterHelper.printServerNotAvailableErrorMessage();
            }
        }
    }

}
