package it.polimi.ingsw.server;
import it.polimi.ingsw.utils.WriterHelper;

/**
 * Main class of server side.
 */
public class LaunchServer {

    private static final String SERVER_INTIALIZATION_ERROR = "Errore nell'istanziare il server.";
    private static final String RELOAD_ARG = "-load";
    private static final String SAVE_ARG = "-save";
    private static String savePath;
    private static String loadPath;

    /**
     * Entry point of the server application.
     * @param args are the configuration arguments. They are empty.
     */
    public static void main(String[] args) {
        try {
            getPathsFromArgs(args);
            MainServer mainServer = new MainServer(savePath);
            if(loadPath != null){
                mainServer.loadExistingGame(loadPath);
                mainServer.addDisconnectedPlayers(mainServer.getGame().getPlayers());
            }
            mainServer.startServer();
        } catch (Exception e) {
            WriterHelper.printErrorMessage(SERVER_INTIALIZATION_ERROR);
        }
    }

    /**
     * Set the path attributes.
     * @param args are the program arguments.
     */
    private static void getPathsFromArgs(String[] args) {
        for(int i=0; i<args.length; i++) {
            if(args[i].equals(RELOAD_ARG)) {
                 loadPath = args[i+1];
            }else if(args[i].equals(SAVE_ARG)){
                savePath = args[i+1];
            }
        }
    }

}
