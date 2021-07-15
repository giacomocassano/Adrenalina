package it.polimi.ingsw.server;

import it.polimi.ingsw.events.client_to_server.ServerEvent;
import it.polimi.ingsw.events.model_to_view.*;
import it.polimi.ingsw.utils.WriterHelper;

import java.io.*;
import java.net.Socket;

/**
 * Socket client connection.
 * It handles socket clients.
 * It receives and sends events on the network.
 */
public class SocketClientConnection extends ClientConnection implements Runnable {
    private static final String CLOSING_CONNECTION_ERROR_MESSAGE = "Errore nel chiudere la connessione!";
    private SocketServer socketServer;
    private Socket clientSocket;

    //Out and In streams
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    /**
     * Constructor.
     * @param socketServer is the socket server.
     * @param clientSocket is the client socket.
     */
    public SocketClientConnection(SocketServer socketServer, Socket clientSocket) {
        super(socketServer);
        this.socketServer = socketServer;
        this.clientSocket = clientSocket;
    }

    /**
     * Override of the run method of Runnable class.
     * It create the server socket connection and wait for events from client.
     */
    @Override
    public void run() {
        try {
            writer = new ObjectOutputStream(clientSocket.getOutputStream());
            reader = new ObjectInputStream(clientSocket.getInputStream());
            asyncSendEvent(new LoginRequest());
            while (isActive()) {
                //Receive the event as socketServer event and visit it as socketServer visitor
                ServerEvent event = receiveEvent();
                event.acceptServerVisitor(this);
                Thread.sleep(10);
            }
        } catch (Exception e) {
            //disconnectConnectionFromServer();
        } finally{
            closeConnection();
        }
    }

    /**
     * Closes the client socket connection.
     */
    private void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            WriterHelper.printErrorMessage(CLOSING_CONNECTION_ERROR_MESSAGE);
        }
        setActive(false);
    }

    /**
     * Receive an event from server
     * @return the event received
     * @throws IOException if there are connection issues.
     * @throws ClassNotFoundException if event cast fails.
     */
    private ServerEvent receiveEvent() throws IOException, ClassNotFoundException {
        return (ServerEvent) reader.readObject();
    }

    /**
     * Send events to the client.
     * @param event is the event to send.
     */
    private synchronized void sendEvent(EventFromModel event) {
        if(isActive()) {
            try {
                writer.writeObject(event);
                writer.flush();
                writer.reset();
            } catch (Exception e) {
                socketServer.setConnectionAsInactive(this);
            }
        }
        if(!event.isUpdateEvent())
            setLastEventSent(event);
    }

    /**
     * Send events to the client in a new thread.
     * @param event is the event to send
     */
    @Override
    public synchronized void asyncSendEvent(EventFromModel event) {
        new Thread(() -> {
            sendEvent(event);
        }).start();
    }

}
