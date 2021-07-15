package it.polimi.ingsw.client;

import it.polimi.ingsw.client.user_interfaces.UserInterface;
import it.polimi.ingsw.events.client_to_server.ServerEvent;
import it.polimi.ingsw.events.model_to_view.EventFromModel;
import it.polimi.ingsw.utils.WriterHelper;

import java.io.*;
import java.net.Socket;

/**
 * Class that handles the socket connection with the server.
 * It extends ServerConnection class.
 */
public class SocketServerConnection extends ServerConnection implements Runnable{

    private static final String SERVER_CONNECTION_ERROR = "Errore nel collegamento col server.";
    private static final String SOCKET_CLOSING_ERROR_MESSAGE = "Errore nel chiudere la socket.";
    private static final String CONNECTION_WITH_SERVER_ERROR = "Errore nel stabilire la connessione con il server.";
    private final Socket socket;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private EventFromModel event;

    /**
     * Constructor.
     * @param client the socket client
     * @param socket the socket used to communicate
     * @param userInterface the chosen user interface
     */
    public SocketServerConnection(Client client, Socket socket, UserInterface userInterface) {
        super(client, userInterface);
        this.socket = socket;
    }

    /**
     * Override of the Runnable class method.
     * It creates the input and output object stream and it receives the events from the client.
     */
    @Override
    public void run(){
        //Getting in and out stream from socket
        try {
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            WriterHelper.printErrorMessage(CONNECTION_WITH_SERVER_ERROR);
        }
        while(isActive()) {
            //While the client is active, it receive the events (from view) from server
            try{
                //Deserialize the event and pass it to the ui interface
                event = receiveEvent();
                getUserInterface().receiveEventFromModel(event);
            }catch (IOException | ClassNotFoundException e) {
                setActive(false);
                closeConnection();
            }
        }
    }

    /**
     * Receive the serialized event from client and deserialize it
     * @return the received event
     * @throws IOException if there are connection issues
     * @throws ClassNotFoundException if the cast from Object to EventFromModel fails
     */
    private EventFromModel receiveEvent() throws IOException, ClassNotFoundException {
        return (EventFromModel) reader.readObject();
    }

    /**
     * Override of asyncSendEvent of ServerConnection class.
     * It sends and event to server in another thread
     * @param event is the event to send
     */
    @Override
    public synchronized void asyncSendEvent(ServerEvent event) {
        new Thread(() -> {
            try {
                writer.writeObject(event);
                writer.flush();
                writer.reset();
            } catch (IOException e) {
                WriterHelper.printErrorMessage(SERVER_CONNECTION_ERROR);
            }
        }).start();
    }

    /**
     * Closes the connection safely
     */
    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            WriterHelper.printErrorMessage(SOCKET_CLOSING_ERROR_MESSAGE);
        }
    }
}
