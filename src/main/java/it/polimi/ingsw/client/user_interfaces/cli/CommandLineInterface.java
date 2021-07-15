package it.polimi.ingsw.client.user_interfaces.cli;

import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.client.user_interfaces.model_representation.EffectInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.EmpowerInfo;
import it.polimi.ingsw.client.user_interfaces.model_representation.WeaponInfo;
import it.polimi.ingsw.events.client_to_server.to_controller.*;
import it.polimi.ingsw.events.model_to_view.LoginSuccess;
import it.polimi.ingsw.exceptions.TimeExceededException;
import it.polimi.ingsw.model.AmmoCubes;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.MoveType;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.ANSIColors;
import it.polimi.ingsw.utils.ReaderHelper;
import it.polimi.ingsw.utils.WriterHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CommandLineInterface class
 is a class that is used to create response events to
 requests coming from the server,
 also shows the client the choices that can be made
 by displaying them from the command line
 *
 *
 */
public class CommandLineInterface {

    private ReaderHelper inFromUser = null;
    private ServerConnection connection;
    private static final String ERROR="Errore nella lettura";
    private static final String CHOICE="Scelta: ";
    private static final String RELOAD_MESSAGE="Hai scelto di ricaricare delle armi.\nSeleziona quale vuoi ricaricare.";
    private static final String USERNAME_REQUEST_MESSAGE="Prego, inserisci un username tra i 3 e i 10 caratteri:";
    private static final String ERROR_NAME_MESSAGE="Esiste già un utente con quel nome. Inseriscine un altro:";
    private static final String ERROR_NAME_LENGHT_MESSAGE="Lunghezza non consentita.Ripeti scelta:\n";
    private static final String CONNECTION_MESSAGE=" ti sei connesso con successo!";
    private static final String RECONNECTION_MESSAGE=" ti sei riconnesso con successo!";
    private static final String CONNECTED_PLAYERS_MESSAGE="Giocatori connessi:\n";
    private static final String WELCOME_MESSAGE="Benvenuto";
    private static final String EMPOWER_BORN_REQUEST_MESSAGE="! Scegli il potenziamento da scartare, verrai rigenerato sul punto generazione di quel colore:\n";
    private static final String REPEAT_CHOICE_MESSAGE="Scelta non idonea.Ripeti scelta: ";
    private static final String REPEAT_CHOICE_WEAPON_MESSAGE="Scelta dell'arma non idonea.Ripeti scelta: ";
    private static final String MOVE_ERROR_MESSAGE="Errore nella lettura della mossa\n";
    private static final String MOVE_REPEAT_CHOICE_MESSAGE="Ripeti scelta della mossa:\n";
    private static final String SQUARE_MESSAGE=" hai scelto di poterti muovere!\n" +
            "Decidi tra queste posizioni:\n";
    private static final String LEFT_MESSAGE="SINISTRA";
    private static final String RIGHT_MESSAGE="DESTRA";
    private static final String TOP_MESSAGE="SOPRA";
    private static final String DOWN_MESSAGE="SOTTO";
    private static final String SAME_SQUARE_MESSAGE="STAI FERMO";
    private static final String GO_BACK_MESSAGE=")Per tornare indietro\n";
    private static final String ERROR_READING_SOMETHING_MESSAGE="Errore nella lettura";
    private static final String SELECT_EMPOWER_MESSAGE=" Hai dei potenziamenti a disposizione, vuoi utilizzarli?\n" +
            "Decidi tra i tuoi disponibili:\n";
    private static final String DONT_USE_MESSAGE="Non utilizzare niente";
    private static final String GRAB_AMMO_MESSAGE=" hai scelto di raccogliere da terra una carta munizione contenente:";
    private static final String GRAB_AMMO_EMPOWER_MESSAGE="E la possibilità di raccogliere un potenziamento\n";
    private static final String ADDICTIONAL_EFFECT_WEAP_MESSAGE="Quest'arma ha un effetto aggiuntivo:";
    private static final String EFFECT_COST="Quest'effetto ha un costo:\n";
    private static final String EFFECT_PAYMENT_QUEST="1)Paga con un potenziamento\n2)Paga con un cubo";
    private static final String MOVEMENT_QUEST="1)Utilizza prima dell'effetto base\n2)Utilizza dopo l'effetto base\n";
    private static final String SELECT_POS_MESSAGE="Scegli le posizioni:\n";
    private static final String DEATH_MESSAGE="\"Sei morto!\nDecidi quale potenziamento scartare,rinascerai nello spawn point del potenziamento scartato\n";

    public void setConnection(ServerConnection connection) {
        this.connection = connection;
    }


    public CommandLineInterface(ServerConnection connection) {
        inFromUser = new ReaderHelper();
        this.connection = connection;
    }

    /**
     * Method to decide player's name.
     * @param isFirstTime is true if is the first spawn.
     * @return a String that is player's name
     */

    public String askName(boolean isFirstTime) {
        if (isFirstTime) {
            WriterHelper.printAdrenalinaTitle();
            WriterHelper.printColored(ANSIColors.BLUE_BOLD,USERNAME_REQUEST_MESSAGE);
        }else
            WriterHelper.printColored(ANSIColors.BLUE_BOLD,ERROR_NAME_MESSAGE);
        String name = "";
        do {
            try {
                name = inFromUser.readLine();
            } catch (IOException e) {
                WriterHelper.printErrorMessage(ERROR);
            }
            if ((name.length()>10|| name.length()<3))
                WriterHelper.printErrorMessage(ERROR_NAME_LENGHT_MESSAGE);
        } while(name.length()>10|| name.length()<3);
        return name.toUpperCase();
    }

    public synchronized void printWaitingPlayers(List<String> playerList) {
        WriterHelper.printColored(ANSIColors.BLUE_BOLD, CONNECTED_PLAYERS_MESSAGE);
        playerList.forEach(p -> WriterHelper.printColored(ANSIColors.BLUE_BOLD, p + " "));
        WriterHelper.printlnOnConsole("");
    }

    /**
     *
     * This method prints to player a message after a reconnection/connection
     * @param type is type of message.
     * @param name is player's name
     * @param message is message displayed
     */
    public synchronized void notifyNameSuccess(int type, String name, String message) {
        if(type == LoginSuccess.INITIAL_LOGIN) {
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, name +CONNECTION_MESSAGE );
        }else if(type == LoginSuccess.RECONNECT_SUCCESS) {
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, name + RECONNECTION_MESSAGE);
        }
    }

    /**
     * Method to create a FirstSpawnResponse from command line interface.
     * @param playerName is player that has to spawn
     * @param empowers are empowers between which the player must choose
     * @return a FirstSpawnResponse.
     * @throws TimeExceededException is time to choose is over.
     */
    public FirstSpawnResponse askBorn(String playerName, List<EmpowerInfo> empowers) throws TimeExceededException {
        WriterHelper.printlnColored(ANSIColors.BLUE_BOLD,WELCOME_MESSAGE + playerName +EMPOWER_BORN_REQUEST_MESSAGE);
        inFromUser = new ReaderHelper();
        for (int i = 0; i < empowers.size(); i++) {
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD,(i + 1) + ") " + empowers.get(i).getName() + " - " + empowers.get(i).getColor());
        }
        WriterHelper.printColored(ANSIColors.CYAN_BOLD,CHOICE);
        int choose = 0;
        do {
            try {
                choose = inFromUser.readUserChoice();
            } catch (IOException | NumberFormatException e) {
                WriterHelper.printColored(ANSIColors.RED_BOLD,ERROR);
            }
            if (!inFromUser.isStopped() && (choose < 1 || choose > empowers.size()))
                WriterHelper.printColored(ANSIColors.RED_BOLD,REPEAT_CHOICE_MESSAGE);
        } while (!inFromUser.isStopped() && (choose < 1 || choose > empowers.size()));
        if (choose != 0) {
            EmpowerInfo empToDrop = empowers.get(choose - 1);
            empowers.remove(empToDrop);
            //Build event to send to model
            FirstSpawnResponse event = new FirstSpawnResponse();
            event.setPlayer(playerName);
            event.setDroppedEmpower(empToDrop);
            event.setOtherEmpower(empowers);
            return event;
        }
        return null;
    }

    /**
     * Method to create a MoveResponse from command line. interface.
     * @param isFinalFrenzy is true if game is on final frenzy mode
     * @param playerName is the player that has to choose his move
     * @param validMoves is a list of valid moves that player can do
     * @param actionNumber is the num of the action. can be 1 or 2
     * @param moveNumber is the num of the move can be 1 to 4/5
     * @return a Move Response event
     * @throws TimeExceededException if time to select move is over
     */
    public synchronized MoveResponse selectMove(boolean isFinalFrenzy, String playerName, List<MoveType> validMoves, int actionNumber, int moveNumber) throws TimeExceededException {
        inFromUser = new ReaderHelper();
        int i=1;
        boolean finish=false;

        if(isFinalFrenzy)
            WriterHelper.printFinalFrenzyTitle();
        WriterHelper.printlnColored(ANSIColors.BLUE_BOLD,playerName+" tocca a te!\n"+"Scegli la "+ANSIColors.MAGENTA_BOLD+moveNumber+ANSIColors.BLUE_BOLD+"° mossa della "+ANSIColors.MAGENTA_BOLD+actionNumber+ANSIColors.BLUE_BOLD+"° azione del tuo turno: ");
        for(MoveType move: validMoves) {
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD,i+") "+move.getDescription());
            i++;
        }
        WriterHelper.printColored(ANSIColors.CYAN_BOLD,CHOICE);
        int choice = 0;
        do {
            try {
                choice=inFromUser.readUserChoice();
            } catch (IOException e) {
                WriterHelper.printColored(ANSIColors.RED_BOLD,MOVE_ERROR_MESSAGE);
            }
            if (choice < 1 || choice >validMoves.size()) {
                WriterHelper.printColored(ANSIColors.RED_BOLD,MOVE_REPEAT_CHOICE_MESSAGE);
            }
            else finish=true;
        } while (!finish);

        return new MoveResponse(playerName, validMoves.get(choice-1));
    }


    /**
     * Method to create a SquareToMoveResponse in command line interface
     * @param validPosition are valid positions where player can move
     * @param playerName is player's name
     * @param myPos is player's position
     * @return a Square to move response
     * @throws TimeExceededException if time to choose is over.
     */
    public synchronized SquareToMoveResponse selectSquare(List<Position> validPosition, String playerName, Position myPos) throws TimeExceededException {
        WriterHelper.printColored(ANSIColors.BLUE_BOLD,playerName+SQUARE_MESSAGE);
        inFromUser = new ReaderHelper();
        String position=null;
        int i=1;
        boolean finish=false;
        boolean abort=false;
        for(Position pos: validPosition){
            if(pos.getRow()==myPos.getRow()){
                if(pos.getColumn()==myPos.getColumn()-1)
                    position=LEFT_MESSAGE;
                else position=RIGHT_MESSAGE;
            }
            else if(pos.getColumn()==myPos.getColumn()){
                if(pos.getRow()==myPos.getRow()-1)
                    position=TOP_MESSAGE;
                else position=DOWN_MESSAGE;
            }
            if(myPos.equals(pos))
                position=SAME_SQUARE_MESSAGE;
            WriterHelper.printColored(ANSIColors.BLUE_BOLD,i+")"+ position+"\n");
            i++;
        }
        WriterHelper.printColored(ANSIColors.RED_BOLD,i+GO_BACK_MESSAGE);
        int choice = 0;
        WriterHelper.printColored(ANSIColors.CYAN_BOLD,CHOICE+"\n");
        do {
            try {
                choice=inFromUser.readUserChoice();
            } catch (IOException e) {
                WriterHelper.printColored(ANSIColors.RED_BOLD,ERROR_READING_SOMETHING_MESSAGE);
            }
            if (choice < 1 || choice >=validPosition.size()+1)
                if (choice == validPosition.size() + 1) {
                    abort = true;
                    finish = true;
                }
                else
                    WriterHelper.printColored(ANSIColors.RED_BOLD,REPEAT_CHOICE_MESSAGE);
            else finish=true;
        } while (!finish);
        if(abort) {
            return new SquareToMoveResponse(playerName, null, true);
        }
        else {
            return new SquareToMoveResponse(playerName,validPosition.get(choice-1),false);
        }
    }

    /**
     * Method in command line interface to select an EmpowerResponse
     * @param playerName is player's name
     * @param validEmpowers are valid empowers that player can choose
     * @return an Empower Response event
     * @throws TimeExceededException if time to choose is over
     */
    public synchronized EmpowerResponse selectEmpower(String playerName,List<EmpowerInfo> validEmpowers) throws TimeExceededException {
        WriterHelper.printColored(ANSIColors.BLUE_BOLD, playerName +SELECT_EMPOWER_MESSAGE );
        inFromUser = new ReaderHelper();
        int i = 1;
        int choice = 0;
        boolean finish = false;
        boolean abort = false;
        for (EmpowerInfo emp : validEmpowers) {
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, i + ")  " + "Nome Potenziamento: " + emp.getName() + " colore:" + emp.getColor().getDescription());
            i++;
        }
        WriterHelper.printlnColored(ANSIColors.RED_BOLD, i + ")  " +DONT_USE_MESSAGE );
        WriterHelper.printColored(ANSIColors.CYAN_BOLD, CHOICE + "\n");
        do {
            try {
                choice = inFromUser.readUserChoice();
            } catch (IOException e) {
                WriterHelper.printColored(ANSIColors.RED_BOLD, ERROR_READING_SOMETHING_MESSAGE);
            }
            if (choice < 1 || choice >= validEmpowers.size() + 1) {
                if (choice == validEmpowers.size() + 1) {
                    finish = true;
                    abort = true;
                } else
                    WriterHelper.printColored(ANSIColors.RED_BOLD, REPEAT_CHOICE_MESSAGE);
            } else {
                finish = true;
            }
        } while (!finish);
        if (abort) {
            return new EmpowerResponse(playerName, null, false);
        } else {
            return new EmpowerResponse(playerName, validEmpowers.get(choice - 1), true);
        }
    }


    /**
     * This is a useful method used when player has to choose a weapon
     * @param playerWeapons are weapons from which player has to choose
     * @return a WeaponInfo
     * @throws TimeExceededException if time to choose is over.
     */
    private WeaponInfo selectWeapon(List<WeaponInfo> playerWeapons) throws TimeExceededException {
        inFromUser = new ReaderHelper();
        int i=1;
        boolean finish=false;
        boolean abort=false;
        for(WeaponInfo weap: playerWeapons){
            WriterHelper.printColored(ANSIColors.BLUE_BOLD,i+")  "+weap.getName()+"\n");
            i++;
        }
        WriterHelper.printColored(ANSIColors.RED_BOLD,i+GO_BACK_MESSAGE);
        int choice = 0;
        do {
            try {
                choice=inFromUser.readUserChoice();
            } catch (IOException e) {
                WriterHelper.printColored(ANSIColors.RED_BOLD,ERROR_READING_SOMETHING_MESSAGE);
            }
            if (choice < 1 || choice >=playerWeapons.size()+1){
                if (choice == playerWeapons.size() + 1) {
                    abort = true;
                    finish = true;
                } else
                    WriterHelper.printColored(ANSIColors.RED_BOLD, REPEAT_CHOICE_WEAPON_MESSAGE);
            }
            else finish=true;
        } while (!finish);
        if (!abort){
            return playerWeapons.get(choice-1);
        }

        else return null;
    }

    private static final String WEAPON_TO_SHOOT_SELECT_MESSAGE=" hai scelto di voler sparare a qualcuno, ora scegli l'arma!\n" +
            "Decidi tra le tue disponibili:";
    /**
     * This method is used in command line to create a WeaponToShootResponse
     * @param playerName is player's name
     * @param playerWeapons are weapons that player can select. (he has to select one)
     * @return a WeaponToShootResponse
     * @throws TimeExceededException is player's time to choose is over.
     */
    public synchronized WeaponToShootResponse selectWeaponToShoot(String playerName, List<WeaponInfo> playerWeapons) throws TimeExceededException {
        WriterHelper.printlnColored(ANSIColors.BLUE_BOLD,playerName+WEAPON_TO_SHOOT_SELECT_MESSAGE);
        WeaponInfo weapon=selectWeapon(playerWeapons);
        inFromUser = new ReaderHelper();
        if(weapon!=null) {
            return new WeaponToShootResponse(playerName, weapon,false);
        }else
            return new WeaponToShootResponse(playerName,weapon,true);
    }

    /**
     *Method to select the weapon effect during a shoot action.
     *
     * @param playerName is the name of the player
     * @param weaponEffects are weapon's effects
     * @param payingEmpowers are empowers that player can use to pay
     * @return a BasicEffectResponse
     * @throws TimeExceededException if time to select is over
     */
    public synchronized BasicEffectResponse selectWeaponEffect(String playerName, List<EffectInfo> weaponEffects,List<EmpowerInfo> payingEmpowers) throws TimeExceededException {
        inFromUser = new ReaderHelper();
        List<EmpowerInfo> empToPay=new ArrayList<>();
        boolean abort=false;
        boolean finish=false;
        WriterHelper.printColored(ANSIColors.BLUE_BOLD,playerName + " hai scelto di voler sparare con l'arma, ecco gli effetti!\n" +
                "Decidi tra:\n");
        int i = 1;
        for (EffectInfo effectInfo : weaponEffects) {
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD,i + ")" + " " + effectInfo.getName());
            WriterHelper.printOnConsole(ANSIColors.BLUE_BOLD+"COSTO:" +ANSIColors.RED_BOLD+"R:" + effectInfo.getCost().getRed()+ANSIColors.YELLOW_BOLD+" Y:" + effectInfo.getCost().getYellow() +ANSIColors.BLUE_BOLD+" B:" + effectInfo.getCost().getBlue()+"\n");
            WriterHelper.printColored(ANSIColors.BLUE_BOLD,effectInfo.getDescription()+"\n");
            i++;
        }
        WriterHelper.printlnColored(ANSIColors.RED_BOLD,i+") Per tornare alla scelta precedente.");
        int choice = 0;
        int choice1 = 0;
        WriterHelper.printColored(ANSIColors.CYAN_BOLD,CHOICE+"\n");
        do {
            try {
                choice = inFromUser.readUserChoice();
            } catch (IOException e) {
                WriterHelper.printErrorMessage("Errore nella lettura dell'effetto");
            }
            if (choice < 1 || choice >=weaponEffects.size()+1) {
                if (choice == weaponEffects.size() + 1) {
                    abort = true;
                    finish=true;
                }
                else
                    WriterHelper.printErrorMessage("Ripeti scelta dell'effetto:");
            }
            else finish=true;
        } while (!finish);
        if(!abort){
            if(!payingEmpowers.isEmpty()&&weaponEffects.get(choice-1).getCost().getRed()!=0 || weaponEffects.get(choice-1).getCost().getBlue()!=0||weaponEffects.get(choice-1).getCost().getYellow()!=0) {
                WriterHelper.printColored(ANSIColors.BLUE_BOLD, "il tuo effetto ha un costo.\nScegli:\n1)Paga utilizzando un potenziamento.\n" +
                        "2)Paga normalmente.\n");
                WriterHelper.printColored(ANSIColors.CYAN_BOLD, CHOICE+"\n");
                choice1 = makeAChoice();
                if(choice1 == 1)
                    empToPay = selectEmpowersToPay(payingEmpowers);
            }
            return new BasicEffectResponse(playerName, false, weaponEffects.get(choice - 1), empToPay);
        }
        else {
            return new BasicEffectResponse(playerName, true, null, empToPay);
        }
    }

    /**
     * Method to help a player to select a weapon to grab
     * @param playerName is player's name
     * @param weaponsToGrab are weapons that player can grab
     * @param weaponsToDrop are weapons that player have to drop
     * @param myEmpowers are player's empowers
     * @return a WeaponToGrabResponse
     * @throws TimeExceededException if time to choose an effect is over.
     */
    public synchronized WeaponToGrabResponse selectWeapontoGrab(String playerName, List<WeaponInfo> weaponsToGrab, List<WeaponInfo> weaponsToDrop, List<EmpowerInfo> myEmpowers) throws TimeExceededException {
        inFromUser = new ReaderHelper();
        WeaponInfo chosenWeapon=null;
        WeaponInfo weaponToDrop=null;
        List<EmpowerInfo> empToPay=new ArrayList<>();
        boolean abort=false;
        boolean finish=false;
        if(!weaponsToDrop.isEmpty()) {
            WriterHelper.printColored(ANSIColors.RED_BOLD, "Prima devi scartare un'arma! decidi tra:\n");
            weaponToDrop = selectWeapon(weaponsToDrop);
        }
        WriterHelper.printColored(ANSIColors.BLUE_BOLD, playerName + " hai scelto di voler raccogliere un'arma, ecco le armi nella tua posizione!\n" +
                "Decidi tra:\n");
        int i = 1;
        for (WeaponInfo weaponInfo : weaponsToGrab) {
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, i + ")  " + "Nome arma: " + weaponInfo.getName());
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, "COSTO DI RICARICA:" + "R:" + weaponInfo.getReloadCost().getRed() + " Y:" + weaponInfo.getReloadCost().getYellow() + " B:" + weaponInfo.getReloadCost().getBlue());
            i++;
        }
        WriterHelper.printlnColored(ANSIColors.RED_BOLD,i+")Per tornare indietro");
        WriterHelper.printColored(ANSIColors.CYAN_BOLD, CHOICE);
        int choice = 0;
        do {
            try {
                choice = inFromUser.readUserChoice();
            } catch (IOException e) {
                WriterHelper.printColored(ANSIColors.RED_BOLD, "Errore nella lettura dell'arma");
            }
            if (choice < 1 || choice >=weaponsToGrab.size()+1) {
                if (choice == weaponsToGrab.size() + 1) {
                    abort = true;
                    finish = true;
                } else
                    WriterHelper.printColored(ANSIColors.RED_BOLD, "Hai inserito un numero non idoneo, ripeti scelta dell'arma.");
            }else {
                chosenWeapon = weaponsToGrab.get(choice - 1);
                finish = true;
            }
        } while (!finish);
        if (!myEmpowers.isEmpty()&&!abort) {
            WriterHelper.printColored(ANSIColors.BLUE_BOLD, "Vuoi pagare l'arma utilizzando dei potenziamenti?\n" +
                    "1)Continua mantenendo i tuoi potenziamenti.\n2)Per pagare utilizzando i potenziamenti.");
            choice=makeAChoice();
            if (choice == 2) {
                empToPay = selectEmpowersToPay(myEmpowers);
            }
        }
        if(abort)
            return new WeaponToGrabResponse(playerName,null,weaponToDrop,empToPay,true);
        else
            return new WeaponToGrabResponse(playerName,chosenWeapon,weaponToDrop,empToPay,false);
    }

    /**
     * Method to help a player to make a choice between two choices
     * @return the number of the chosen choice
     * @throws TimeExceededException if time is over to make the choice
     */
    private int makeAChoice() throws TimeExceededException {
        inFromUser = new ReaderHelper();
        int choice=0;
        do {
            try {
                choice = inFromUser.readUserChoice();
            } catch (IOException e) {
                WriterHelper.printColored(ANSIColors.RED_BOLD, ERROR);
            }
            if ((choice != 1 && choice != 2))
                WriterHelper.printColored(ANSIColors.RED_BOLD, "Hai inserito un numero non idoneo, ripeti scelta");
        } while ((choice != 1 && choice != 2));
        return choice;
    }

    /**
     * Method to help player to make a choice between some choices
     * @param min is the min option that player can select
     * @param max is the max option that player can select
     * @return the number of the choice
     * @throws TimeExceededException if time is over to make a choice
     */
    private int makeAChoice2(int min,int max) throws TimeExceededException {
        inFromUser = new ReaderHelper();
        int choice=0;
        do {
            try {
                choice = inFromUser.readUserChoice();
            } catch (IOException e) {
                WriterHelper.printColored(ANSIColors.RED_BOLD, ERROR);
            }
            if ((choice<min||choice>max))
                WriterHelper.printColored(ANSIColors.RED_BOLD, REPEAT_CHOICE_MESSAGE);
        } while (choice<min||choice>max);
        return choice;
    }


    /**
     * Method to help player to select some empowers to pay something
     * @param myEmpowers are player's empowers
     * @return a list of empowers infos that are chosen empowers
     * @throws TimeExceededException if time to choose empowers is over
     */
    private List<EmpowerInfo> selectEmpowersToPay(List<EmpowerInfo> myEmpowers) throws TimeExceededException {
        inFromUser = new ReaderHelper();
        int choice=0;
        int i;
        boolean flag=true;
        List<EmpowerInfo> empToPay=new ArrayList<>();
        WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, "hai scelto di pagare con i tuoi potenziamenti. Inserisci nella lista quelli che vuoi scartare.");
        WriterHelper.printColored(ANSIColors.CYAN_BOLD, CHOICE);
        do {
            i = 1;
            for (EmpowerInfo emp : myEmpowers) {
                WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, i + ") " + emp.getName() + " " + emp.getColor().getDescription());
                i++;
            }
            WriterHelper.printlnColored(ANSIColors.RED_BOLD, i + ") per terminare la scelta.");
            try {
                choice = inFromUser.readUserChoice();
            } catch (IOException e) {
                WriterHelper.printColored(ANSIColors.RED_BOLD,ERROR);
            }
            if (!inFromUser.isStopped() && (choice < 1 || choice >=myEmpowers.size()+1)) {
                if (choice == myEmpowers.size()+1) {
                    WriterHelper.printColored(ANSIColors.GREEN_BOLD, "Hai scelto di terminare");
                    flag = false;
                }
                else
                    WriterHelper.printlnColored(ANSIColors.RED_BOLD, REPEAT_CHOICE_MESSAGE);
            }
            else {
                if (!empToPay.contains(myEmpowers.get(choice - 1))) {
                    empToPay.add(myEmpowers.get(choice - 1));
                    myEmpowers.remove(choice-1);
                }
            }
        }
        while (flag);
        return empToPay;
    }

    /**
     * Method to help player to select the use of an empower
     * @param playerName is player's name
     * @param empower is the empower
     * @param enemies are enemies that player can hit with the empower
     * @param positions are position where player can move with the empower
     * @param empowers are empowers that player has
     * @param cubes are player's cubes
     * @param payable is true if player has to pay the empower
     * @return an Empower use response obj
     * @throws TimeExceededException if time to chose the empower is over.
     */
    public EmpowerUseResponse selectEmpowerUse(String playerName,EmpowerInfo empower,List<String> enemies, List<Position> positions,
                                               List<EmpowerInfo> empowers, AmmoCubes cubes,boolean payable)throws TimeExceededException {
        int choice = 0;
        boolean abort = false;
        List<String> targets;
        String target=null;
        Position pos=null;
        List<EmpowerInfo> empToPay;
        EmpowerInfo emp=null;
        List<Position> newPositions;
        Color payingColor=null;
        WriterHelper.printColored(ANSIColors.BLUE_BOLD, "Hai scelto di utilizzare il tuo potenziamento: " + empower.getName() + " " + empower.getColor().getDescription() + ".\n");
        WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, empower.getDescription());
        if (payable) {
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, "Il potenziamento ha un costo.\n1)Paga con un altro potenziamento.2)Paga con un cubo.");
            choice = makeAChoice();
            if (choice == 1) {
                WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, "Seleziona un potenziamento solamente!");
                empToPay = selectEmpowersToPay(empowers);
                if (empToPay.size() != 1) {
                    WriterHelper.printColored(ANSIColors.RED_BOLD, "Hai optato per una scelta sbagliata. Ritorno alla mossa precedente\n");
                    abort = true;
                }
                else
                    emp=empToPay.get(0);
            } else {
                payingColor = selectPayingCubes(cubes);
                if(payingColor==null)
                    abort=true;
            }
        }
        if (!enemies.isEmpty() && !abort) {
            WriterHelper.printColored(ANSIColors.BLUE_BOLD, "Decidi un nemico da colpire!\n");
            targets = selectPlayers(new ArrayList<>(enemies), 1);
            if (targets == null||targets.size()>1)
                abort = true;
            else target=targets.get(0);
        }
        if (!positions.isEmpty() && !abort) {
            WriterHelper.printColored(ANSIColors.BLUE_BOLD, "Decidi una posizione dove spostarti!\n");
            newPositions = selectPositions(positions, 1);
            if(newPositions==null)
                abort=true;
            else pos=newPositions.get(0);
        }
        if (positions.isEmpty() && enemies.isEmpty()) {
            WriterHelper.printColored(ANSIColors.RED_BOLD, "Non puoi fare nulla con questo potenziamento. Ritorno alla mossa precedente\n");
            abort = true;
        }
        if (abort)
            return new EmpowerUseResponse(playerName,empower,target,pos,payingColor,emp,true);
        else return new EmpowerUseResponse(playerName,empower,target,pos,payingColor,emp,false);
    }

    /**
     * Method to select cubes to pay.
     * @param cubes are player cubes
     * @return a color of the cube to pay
     * @throws TimeExceededException if time to pay with a cube is over.
     */
    public Color selectPayingCubes(AmmoCubes cubes)throws TimeExceededException {
        int choice;
        WriterHelper.printOnConsole(ANSIColors.BLUE_BOLD + "Seleziona quale cubo vuoi usare\n" + ANSIColors.RED_BOLD + "1)ROSSO\n" + ANSIColors.BLUE_BOLD + "2)BLU\n" + ANSIColors.YELLOW_BOLD + "3)GIALLO\n");
        choice = makeAChoice2(1, 3);
        if ((choice == 1 && cubes.getRed() > 0) || (choice == 2 && cubes.getBlue() > 0) || choice == 3 && cubes.getYellow() > 0) {
            if (choice == 1)
                return Color.RED;
            else if (choice == 2)
                return Color.BLUE;
            else if (choice == 3)
                return Color.YELLOW;
        }
        return null;
    }

    /**
     * Method to help player to select a weapon basic effect.
     * @param playerName is player's name
     * @param players is a list of targets that player can hit
     * @param squares is a list of squares where player can shoot
     * @param maxTargets are max targets to select by player
     * @param maxSquares are max squares to select by player
     * @param recoilPositions are positions to recoil the enemy
     * @return a Basic effect use response
     * @throws TimeExceededException if time to select the basic effect is over
     */
    public synchronized BasicEffectUseResponse selectBasicEffectUse(String playerName,List<String> players, List<Position> squares,int maxTargets, int maxSquares, List<Position> recoilPositions) throws TimeExceededException {
        inFromUser = new ReaderHelper();
        List<String> enemies=new ArrayList<>();
        Position recoilPos=new Position();
        List<Position> positions=new ArrayList<>();
        boolean abort=false;
        WriterHelper.printColored(ANSIColors.BLUE_BOLD,playerName+" hai scelto di usare un effetto!");
        if(maxTargets>0){
            WriterHelper.printColored(ANSIColors.BLUE_BOLD,"Puoi sparare ad "+maxTargets+" nemico/i. Selenzionalo/i:\n");
            enemies=selectPlayers(players,maxTargets);
            if(enemies==null)
                abort=true;
        }
        if(!recoilPositions.isEmpty()&&!abort) {
            WriterHelper.printColored(ANSIColors.BLUE_BOLD,"Scegli dove rinculare il tuo nemico/i!:\n");
            recoilPos = selectPositions(recoilPositions, 1).get(0);
        }
        if(maxSquares>0&&!squares.isEmpty()&&!abort) {
            WriterHelper.printColored(ANSIColors.BLUE_BOLD, "Puoi sparare al più in " + maxSquares + " caselle. Selenzionale:\n");
            positions = selectPositions(squares, maxSquares);
            if(positions==null)
                abort=true;
        }
        WriterHelper.printColored(ANSIColors.GREEN,"sto inviando"+abort+" "+enemies+" "+positions+" "+recoilPos);
        return new BasicEffectUseResponse(abort,playerName,enemies,positions,recoilPos);
    }

    /**
     *Method to select player's respawn point
     * @param playerName is player's name
     * @param empowers are player's empowers
     * @return a respawn response
     * @throws TimeExceededException if time to select the respawn point is over.
     */
    public synchronized RespawnResponse selectRespawn(String playerName, List<EmpowerInfo> empowers)throws TimeExceededException{

        int choice=0;
        int i=1;
        boolean finish=false;
        WriterHelper.printColored(ANSIColors.BLUE_BOLD,DEATH_MESSAGE);
        for(EmpowerInfo p: empowers) {
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, i + ") " + p.getName() + "  " + p.getColor());
            i++;
        }
        WriterHelper.printColored(ANSIColors.CYAN_BOLD,CHOICE);
        do {
            try {
                choice=inFromUser.readUserChoice();
            } catch (IOException e) {
                WriterHelper.printColored(ANSIColors.RED_BOLD,"Errore nella lettura del potenziamento\n");
            }
            if (choice < 1 || choice >empowers.size()) {
                WriterHelper.printColored(ANSIColors.RED_BOLD, "Ripeti scelta del potenziamento\n");
            }
            else finish=true;
        } while (!finish);
        return new RespawnResponse(playerName, empowers.get(choice - 1));
    }

    /**
     * Method to select players
     * @param players are players to select
     * @param max is the max number of players to select
     * @return a list of players names
     * @throws TimeExceededException if time to select players is over.
     */
    private List<String> selectPlayers(List<String> players,int max) throws TimeExceededException {
        int i;
        inFromUser = new ReaderHelper();
        int choice = 0;
        int cont = 0;
        boolean finish = false;
        boolean abort = false;
        List<String> newPlayers = new ArrayList<>();
        do {
            i = 1;
            for (String s : players) {
                WriterHelper.printColored(ANSIColors.BLUE_BOLD, i + ")" + s + "\n");
                i++;
            }
            if (!newPlayers.isEmpty())
                WriterHelper.printColored(ANSIColors.BLUE_BOLD, i + ") per terminare la selezione\n");
            WriterHelper.printColored(ANSIColors.RED_BOLD, (i + 1) + ") per tornare indietro\n");
            try {
                choice = inFromUser.readUserChoice();
            } catch (IOException e) {
                WriterHelper.printColored(ANSIColors.RED_BOLD, ERROR_READING_SOMETHING_MESSAGE);
            }
            if (choice < 1 || choice >= players.size() + 1) {
                if (choice == players.size() + 1 && !newPlayers.isEmpty())
                    finish = true;
                else if (choice == players.size() + 2) {
                    abort = true;
                    finish = true;
                } else
                    WriterHelper.printColored(ANSIColors.RED_BOLD, REPEAT_CHOICE_MESSAGE);
            } else {
                if (!newPlayers.contains(players.get(choice - 1))) {
                    newPlayers.add(players.get(choice - 1));
                    players.remove(choice - 1);
                    cont++;
                    if (cont != max)
                        WriterHelper.printColored(ANSIColors.GREEN_BOLD, "Scegli un'altro nemico\n");
                }
            }
        }
        while (cont < max && !finish);
        if (abort) {
            return null;
        } else {
            return newPlayers;
        }
    }

    /**
     * Helps player to select some positions
     * @param squares is a list of some squares
     * @param max is the max num of squares that player can select
     * @return a list of selected squares
     * @throws TimeExceededException  if time to select the positions is over
     */
    private List<Position> selectPositions(List<Position> squares,int max) throws TimeExceededException {
        int choice=0;
        int cont=0;
        boolean finish=false;
        boolean abort=false;
        int i;
        inFromUser = new ReaderHelper();
        List<Position> newPositions=new ArrayList<>();
        do {
            if(cont!=0)
                WriterHelper.printColored(ANSIColors.BLUE_BOLD,"Scegli un'altra posizione!\n");
            i=1;
            for(Position pos: squares) {
                WriterHelper.printColored(ANSIColors.BLUE_BOLD, i + ") " + "R:" + pos.getRow() + " C:" + pos.getColumn() + "\n");
                i++;
            }
            if(!newPositions.isEmpty())
                WriterHelper.printColored(ANSIColors.BLUE_BOLD,i+") per terminare la selezione.\n");
            WriterHelper.printColored(ANSIColors.RED_BOLD,(i+1)+") per tornare indietro.\n");
            try {
                choice=inFromUser.readUserChoice();
            } catch (IOException e) {
                WriterHelper.printColored(ANSIColors.RED_BOLD,ERROR_READING_SOMETHING_MESSAGE);
            }
            if (choice < 1 || choice >=squares.size()+1){
                if (choice == squares.size() + 1 && !newPositions.isEmpty())
                    finish=true;
                else if (choice == squares.size()+2) {
                    abort = true;
                    finish=true;
                }
                else
                    WriterHelper.printColored(ANSIColors.RED_BOLD,ERROR);
            }
            else{
                if(!newPositions.contains(squares.get(choice-1))){
                    newPositions.add(squares.get(choice-1));
                    squares.remove(choice-1);
                    cont++;

                }
            }
        } while (cont < max && !finish);
        if(abort) return null;
        return newPositions;
    }

    /**
     * Method to help player to select a weapon to reload
     * @param weaponsToReload are weapons that player wants to reload
     * @param playerName is player's name
     * @param myCubes are player's ammo cubes
     * @param myEmpowers are player's empowers
     * @return a Weapons to reload response
     * @throws TimeExceededException if time to reload weapons is over
     */

    public synchronized WeaponsToReloadResponse selectWeaponToReload(List<WeaponInfo> weaponsToReload, String playerName, AmmoCubes myCubes,List<EmpowerInfo> myEmpowers) throws TimeExceededException {
        WeaponInfo weaponToReload;
        inFromUser = new ReaderHelper();
        List<EmpowerInfo> empToPay=new ArrayList<>();
        List<WeaponInfo> weaponsReloaded=new ArrayList<>();
        List<EmpowerInfo> finalemps=new ArrayList<>();
        int blue;
        int myBlue;
        int red;
        int myRed;
        int yellow;
        int myYellow;
        int choice;
        boolean flag=true;
        boolean abort=false;
        myBlue=myCubes.getBlue();
        myRed=myCubes.getRed();
        myYellow=myCubes.getYellow();
        do {
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD,RELOAD_MESSAGE);
            weaponToReload = selectWeapon(weaponsToReload);
            if(weaponToReload!=null) {
                red = weaponToReload.getBuyCost().getRed();
                blue = weaponToReload.getBuyCost().getBlue();
                yellow = weaponToReload.getBuyCost().getYellow();
                if (!myEmpowers.isEmpty()) {
                    WriterHelper.printColored(ANSIColors.BLUE_BOLD, "Hai dei potenziamenti da utilizzare:\n" +
                            "1)Scarta un potenziamento per pagare un cubo\n2)Procedi senza scartare\n");
                    choice = makeAChoice();
                    if (choice == 1) {
                        empToPay = selectEmpowersToPay(myEmpowers);
                        for (EmpowerInfo e : empToPay) {
                            switch (e.getColor()) {
                                case RED:
                                    red--;
                                    break;
                                case BLUE:
                                    blue--;
                                    break;
                                case YELLOW:
                                    yellow--;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                myRed = myRed - red;
                myBlue = myBlue - blue;
                myYellow = myYellow - yellow;
                if (myBlue >= 0 && myRed >= 0 && myYellow >= 0) {
                    WriterHelper.printlnColored(ANSIColors.GREEN_BOLD, "L'arma è ricaricabile");
                    weaponsReloaded.add(weaponToReload);
                    weaponsToReload.remove(weaponToReload);
                    for (EmpowerInfo e : empToPay) {
                        myEmpowers.remove(e);
                        finalemps.add(e);
                    }
                    WriterHelper.printColored(ANSIColors.BLUE_BOLD, "Decidi se ricaricare un'altra arma o se terminare.\n" +
                            "1) ricarica un'altra arma\n2)termina\n");
                    choice = makeAChoice();
                    if(choice==1)
                        flag = true;
                    else
                        flag = false;
                }
            }
            else flag=false;
        }while (flag);
        WriterHelper.printColored(ANSIColors.BLUE_BOLD,"Sicuro della scelta o vuoi tornare indietro?\n1)PROCEDI\n"+ANSIColors.RED_BOLD+"2)TORNA INDIETRO\n");
        choice=makeAChoice();
        if(choice==2)
            abort=true;
        return new WeaponsToReloadResponse(playerName,weaponsReloaded,finalemps,abort);
    }

    /**
     * Shows to a player a certain message on console
     * @param message is the message
     */
    public void showMessage(String message) {
        WriterHelper.printlnOnConsole(message);
    }

    /**
     * Method to help player to select how to use the ultra effect
     * @param playerName is player's name
     * @param players are enemy players
     * @param squares are squares where player can hit with the ultra effect
     * @param maxTargets is the max num of targets that player can hit
     * @param maxSquares is the max num of squares that player can hit
     * @return an Ultra Effect use response
     * @throws TimeExceededException if time to select the ultra effect use is over
     */
    public synchronized UltraEffectUseResponse selectUltraEffectUse(String playerName,List<String> players, List<Position> squares,int maxTargets, int maxSquares) throws TimeExceededException {
        inFromUser = new ReaderHelper();
        List<String> enemies=new ArrayList<>();
        List<Position> positions;
        Position position = null;
        boolean abort=false;
        WriterHelper.printlnColored(ANSIColors.BLUE_BOLD,playerName+" hai scelto di usare un effetto ultra!");
        if(maxTargets>0){
            WriterHelper.printColored(ANSIColors.BLUE_BOLD,"Puoi sparare ad "+maxTargets+" nemici. Selenzionalo/i:\n");
            enemies=selectPlayers(players,maxTargets);
            if(enemies==null)
                abort=true;
        }
        if(maxSquares>0&&!squares.isEmpty()&&!abort) {
            WriterHelper.printColored(ANSIColors.BLUE_BOLD, "Puoi sparare al massimo in " + maxSquares + " caselle. Selenzionale:\n");
            positions = selectPositions(squares, maxSquares);
            if(positions==null)
                abort=true;
            else if(!positions.isEmpty())
                position = positions.get(0);
        }
        return new UltraEffectUseResponse(abort,playerName,enemies,position);
    }

    /**
     *
     * @param playerName is player's name
     * @param effect is the effect that player has to choose
     * @return an Ultra effect response
     * @throws TimeExceededException if time to choose the ultra effect is over
     */
    public synchronized UltraEffectResponse selectWeaponUltraEffect(String playerName,EffectInfo effect) throws TimeExceededException {
        List<EmpowerInfo> empToPay=new ArrayList<>();
        List<EmpowerInfo> payingEmpowers=new ArrayList<>();
        inFromUser = new ReaderHelper();
        int choice;
        WriterHelper.printColored(ANSIColors.BLUE_BOLD,playerName + ", la tua arma dispone di un'effetto aggiuntivo! \n");
        WriterHelper.printlnColored(ANSIColors.BLUE_BOLD,  "Nome effetto:" + effect.getName());
        WriterHelper.printOnConsole(ANSIColors.CYAN_BOLD+"COSTO:" +ANSIColors.RED_BOLD+ "R:" + effect.getCost().getRed() + ANSIColors.YELLOW_BOLD+" Y:" + effect.getCost().getYellow() + ANSIColors.BLUE_BOLD +" B:" + effect.getCost().getBlue()+"\n");
        WriterHelper.printlnColored(ANSIColors.BLUE_BOLD,effect.getDescription());
        WriterHelper.printOnConsole(ANSIColors.BLUE_BOLD+"Vuoi utilizzarlo?\n"+ANSIColors.CYAN_BOLD+"1)SI\n"+ANSIColors.RED_BOLD+"2)NO\n");
        WriterHelper.printColored(ANSIColors.CYAN_BOLD,CHOICE+"\n");
        choice=makeAChoice();
        if(choice==1) {
            if(effect.getCost().getRed()!=0 || effect.getCost().getBlue()!=0||effect.getCost().getYellow()!=0) {
                WriterHelper.printColored(ANSIColors.BLUE, "il tuo effetto ha un costo. Scegli:\n1)Paga utilizzando un potenziamento\n" +
                        "2)Paga normalmente.\n");
                WriterHelper.printColored(ANSIColors.CYAN_BOLD, "La tua scelta:");
                choice = makeAChoice();
                if (choice == 1)
                    empToPay = selectEmpowersToPay(payingEmpowers);
            }
            WriterHelper.printOnConsole(ANSIColors.BLUE+"Procedere?\n"+ANSIColors.CYAN_BOLD+"1)SI\n"+ANSIColors.RED_BOLD+"2)NO\n");
            choice=makeAChoice();
            if(choice==1)
                return new UltraEffectResponse(playerName, false, true, empToPay);
            else
                return new UltraEffectResponse(playerName,true,false, empToPay);
        }
        else return new UltraEffectResponse(playerName, false,false, empToPay);
    }

    /**
     * Method to select the shooter movement response
     * @param playerName is client's player name
     * @param positions is a list of valid position to move in
     * @param empowers are player's empowers
     * @param effect is the movement effect
     * @return a ShooterMovementResponse
     * @throws TimeExceededException if time to choose response is over
     */
    public ShooterMovementResponse selectMovement(String playerName,List<Position> positions,List<EmpowerInfo> empowers,EffectInfo effect)throws TimeExceededException{

        boolean firstBase=false;
        Position pos=null;
        boolean abort=false;
        List<EmpowerInfo> empToPay=null;
        int choice;
        List<Position> newPositions;
        WriterHelper.printColored(ANSIColors.BLUE_BOLD, ADDICTIONAL_EFFECT_WEAP_MESSAGE);
        WriterHelper.printlnColored(ANSIColors.BLUE_BOLD,effect.getName()+": "+effect.getDescription());
        WriterHelper.printColored(ANSIColors.BLUE_BOLD,MOVEMENT_QUEST);
        WriterHelper.printColored(ANSIColors.CYAN_BOLD,CHOICE);
        choice=makeAChoice();
        if(choice==1)
            firstBase=true;
        WriterHelper.printColored(ANSIColors.CYAN_BOLD,SELECT_POS_MESSAGE);
        newPositions=selectPositions(positions,1);
        if(newPositions==null||newPositions.size()>1) {
            abort = true;
        }
        else pos=newPositions.get(0);
        if(!abort&&(effect.getCost().getRed()>0||effect.getCost().getYellow()>0||effect.getCost().getBlue()>0)) {
            WriterHelper.printColored(ANSIColors.BLUE_BOLD,EFFECT_COST );
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, "B:" + effect.getCost().getBlue());
            WriterHelper.printColored(ANSIColors.BLUE_BOLD, EFFECT_PAYMENT_QUEST);
            WriterHelper.printColored(ANSIColors.CYAN_BOLD, CHOICE);
            choice = makeAChoice();
            if (choice == 1) {
                empToPay = selectEmpowersToPay(empowers);
                if (empToPay.size()!=1)
                    abort = true;

            }
        }
        return new ShooterMovementResponse(playerName,!abort,pos,firstBase,empToPay);
    }

    /**
     * This method creates a GrabAmmoResponse after picking an ammo on map
     * @param playerName is client's name
     * @param ammo is an ammo card
     * @param hasEmpower is true if card has an empower
     * @return a GrabAmmoResponse
     * @throws TimeExceededException if time to grab an ammo is over
     */

    public GrabAmmoResponse acceptAmmo(String playerName, AmmoCubes ammo,boolean hasEmpower)throws TimeExceededException{
        int choice=0;
        WriterHelper.printColored(ANSIColors.BLUE_BOLD, playerName+ GRAB_AMMO_MESSAGE);
        WriterHelper.printOnConsole(ANSIColors.RED_BOLD+ "R:" + ammo.getRed() + ANSIColors.YELLOW_BOLD+" Y:" + ammo.getYellow() + ANSIColors.BLUE_BOLD +" B:" + ammo.getBlue()+"\n");
        if(hasEmpower)
            WriterHelper.printColored(ANSIColors.BLUE_BOLD,GRAB_AMMO_EMPOWER_MESSAGE);
        WriterHelper.printOnConsole(ANSIColors.BLUE_BOLD+"1)Procedi\n"+ANSIColors.RED_BOLD+"2)Torna alla scelta precedente\n");
        WriterHelper.printColored(ANSIColors.CYAN_BOLD, CHOICE);
        choice=makeAChoice();
        if(choice==1)
            return new GrabAmmoResponse(playerName,false);
        else if(choice==2)
            return new GrabAmmoResponse(playerName,true);
        return null;
    }

    /**
     * Methos to print the waiting room
     * @param connected is player recently connected
     * @param disconnected is player recently disconnected
     * @param waitings are players that are waiting
     * @param isStarting true if game is starting
     * @param timer is time before starting
     */
    public void printWaitingRoom(String connected, String disconnected, List<String> waitings, boolean isStarting, int timer) {
        WriterHelper.printWhiteRows(50);
        WriterHelper.printAdrenalinaTitle();
        if(!waitings.isEmpty()) {
            String leftAlignFormat = "| %-15s | %-4d |%n";
            WriterHelper.printColored(ANSIColors.GREEN_BOLD);
            System.out.format("+-----------------+------+%n");
            System.out.format("| Giocatore       |  #   |%n");
            System.out.format("+-----------------+------+%n");
            for (int i = 0; i < waitings.size(); i++) {
                System.out.format(leftAlignFormat, waitings.get(i), i+1);
            }
            System.out.format("+-----------------+------+%n");
            WriterHelper.printColored(ANSIColors.RESET);
        }
        if(connected != null)
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, connected + " si è connesso.");
        if(disconnected != null)
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, disconnected + " si è disconnesso.");
        if(isStarting)
            WriterHelper.printlnColored(ANSIColors.BLUE_BOLD, "La partita inizierà tra: "+ANSIColors.YELLOW_BOLD+ timer);
    }

    /**
     * Method to stop player writing from keyboard
     */
    public void stopRead() {
        if(inFromUser != null)
            inFromUser.stop();
    }
}