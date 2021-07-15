package it.polimi.ingsw.client;

import it.polimi.ingsw.client.user_interfaces.UserInterface;
import it.polimi.ingsw.client.user_interfaces.cli.CLIController;
import it.polimi.ingsw.client.user_interfaces.gui.GUIcontroller;
import it.polimi.ingsw.utils.ANSIColors;
import it.polimi.ingsw.utils.Config;
import it.polimi.ingsw.utils.ReaderHelper;
import it.polimi.ingsw.utils.WriterHelper;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class of the client application.
 */
public class LaunchClient extends Application{

    private static GUIcontroller loginSession;

    /**
     * Entry point of the client application.
     * @param args the configuration arguments
     */
    public static void main(String[] args) {
        Config config = Config.buildConfig(args);
        UserInterface userInterface = null;
        boolean isFirstRequest = true;
        //Check ui method
        if(config.getUiMethod() == null && args.length > 0) {
            config.setUiMethod(args[0]);
        }
        //If ui method is wrong
        if((config.getUiMethod() == null) || (config.getUiMethod() != null && !config.getUiMethod().equals("cli") && !config.getUiMethod().equals("gui"))) {
            WriterHelper.printAdrenalinaTitle();
            int ui = askUIMethod();
            if(ui == 1) config.setUiMethod("gui");
            else config.setUiMethod("cli");
            isFirstRequest = false;
        }
        //Initialize ui
        if(config.getUiMethod().equals("cli")) {
            userInterface = new CLIController(config);
        }else if (config.getUiMethod().equals("gui")){
            loginSession=new GUIcontroller(config);
            userInterface=loginSession;
            try {
                new Thread(()->launch(args)).start();
            }
            catch (Exception e){
                WriterHelper.printErrorMessage("Errore nella gui");
            }
        }
        //If connection method is wrong
        if(config.getUiMethod().equals("cli")) {
            if (config.getCommunicationMethod() == null || (!config.getCommunicationMethod().equals("soc") && !config.getCommunicationMethod().equals("rmi"))) {
                if (isFirstRequest)
                    WriterHelper.printAdrenalinaTitle();
                int conn = askConnectionMethod();
                if (conn == 1) config.setCommunicationMethod("soc");
                else config.setCommunicationMethod("rmi");
            }
            //Initialize server
            if (config.getCommunicationMethod().equals("soc")) {
                SocketClient socketClient = new SocketClient(config.getServerIp(), config.getSocPort(), userInterface);
                socketClient.startClient();
            } else if (config.getCommunicationMethod().equals("rmi")) {
                RMIClient rmiClient = new RMIClient(config.getServerIp(), config.getRmiPort(), userInterface);
                rmiClient.startClient();
            }
        }
    }

    /**
     *
     * @param stage the initial stage of the window
     * @throws Exception when there are java fx errors
     */
    @Override
    public void start(Stage stage) throws Exception {
        loginSession.start(new Stage());
    }

    /**
     * Asks the user interface preferred method to the user.
     * It can be Graphic User Interface or Command Line Interface.
     * @return the chosen user interface
     */
    private static int askUIMethod() {
        WriterHelper.printColored(ANSIColors.BLUE_BOLD, "Benvenuto! Vuoi utilizzare la gui o la cli?\n" +
                "1. GUI\n" +
                "2. CLI\n" +
                "Scelta: ");
        ReaderHelper reader = new ReaderHelper();
        int choice = 0;
        do{
            try {
                choice = Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                choice = 0;
            }
        }while(choice < 1 || choice > 2);
        return choice;
    }

    /**
     * Asks the preferred communication method to the user.
     * It can be Socket or RMI.
     * @return the chose connection method
     */
    private static int askConnectionMethod() {
        WriterHelper.printColored(ANSIColors.BLUE_BOLD, "Vuoi utilizzare socket o rmi?\n" +
                "1. Socket\n" +
                "2. RMI\n" +
                "Scelta: ");
        ReaderHelper reader = new ReaderHelper();
        int choice = 0;
        do{
            try {
                choice = Integer.parseInt(reader.readLine());
            } catch (IOException | NumberFormatException e) {
                choice = 0;
            }
        }while(choice < 1 || choice > 2);
        return choice;
    }



}
