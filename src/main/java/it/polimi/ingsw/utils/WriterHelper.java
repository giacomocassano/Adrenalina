package it.polimi.ingsw.utils;
import java.util.Map;

/**
 * Class used to print some messages in command line interface,
 * Used to remove lots of usages of system.out.print() and to print some draws during
 * login and in the winning screen.
 */

public class WriterHelper {

    private static final String MESSAGE_SERVER_ERROR="Mi dispiace! Non riesco a connettermi con il server.\n" +
            "Potrebbe essersi verificato uno tra i seguenti problemi:\n" +
            "\t1) Il tuo computer non è connesso in rete\n" +
            "\t2) Il server potrebbe essere al momento non disponibile\n" +
            "Ti consigliamo di provare più tardi.";

    /**
     * prints on console with \n
     * @param message is the message to print
     */
    public static void printlnOnConsole(String message) {
        System.out.println(message);
    }

    /**
     * prints on console without \n
     * @param message is the message to print
     */
    public static void printOnConsole(String message){
        System.out.print(message);
    }

    /**
     * prints on console with \n and colored used ANSI escape codes
     * @param color is the color that is used to print
     * @param message is the message
     */
    public static void printlnColored(ANSIColors color, String message) {
        System.out.print(color);
        printOnConsole(message);
        System.out.print(ANSIColors.RESET);
        printlnOnConsole("");
    }

    /**
     * Prints a red bold error message
     * @param errorMessage is the error message
     */
    public static void printErrorMessage(String errorMessage) {
        printColored(ANSIColors.RED_BOLD, "[ERR] " + errorMessage);
    }

    /**
     * prints colored without \n using ansi escape codes
     * @param color is the color wanted to print colored
     */
    public static void printColored(ANSIColors color){
        System.out.print(color);
    }

    /**
     * prints with a tag. used server side for threads
     * @param tag is the wanted tag
     * @param message is the message to print
     */
    public static void printWithTag(String tag, String message) {
        printlnOnConsole("[" + tag + "] "+message);
    }

    /**
     * prints the title of the game
     */
    public static void printAdrenalinaTitle() {
        WriterHelper.printlnOnConsole("");
        WriterHelper.printOnConsole(ANSIColors.YELLOW_BOLD+"   ___   ___  ___  _____  _____   __   _____  _____ \n" +
                ANSIColors.YELLOW_BOLD+"  / _ | / _ \\/ _ \\/ __/ |/ / _ | / /  /  _/ |/ / _ |\n" +
                ANSIColors.BLUE_BOLD+" / __ |/ // / , _/ _//    / __ |/ /___/ //    / __ |\n" +
                ANSIColors.YELLOW_BOLD+"/_/ |_/____/_/|_/___/_/|_/_/ |_/____/___/_/|_/_/ |_|\n");
        WriterHelper.printOnConsole("   __  ___  ____   ___\n" +
                "  /  |/  / |_  /  <  /\n" +
                " / /|_/ / _/_ <   / / \n" +
                "/_/  /_/ /____/  /_/  \n" +
                "                      \n");
        WriterHelper.printlnColored(ANSIColors.BLUE_BOLD,"Progetto di Ingegneria del Software A.A. 2018/2019\n\n" +
                "Docente:Alessandro Margara\n\n" +
                "Responsabili di laboratorio: Michele Bertoni - Gian Enrico Conti - Andrea Corsini - Niccolò Izzo\n");
        WriterHelper.printlnColored(ANSIColors.BLUE_BOLD,"Alunni:  Giacomo Cassano  -  Silvano Bianchi  -  Antonio Castronuovo");
        WriterHelper.printlnOnConsole("");
        WriterHelper.printlnOnConsole("");
        WriterHelper.printlnOnConsole("");
    }

    /**
     * Prints final frenzy title
     */
    public static void printFinalFrenzyTitle() {
        WriterHelper.printOnConsole("\n" +
                ANSIColors.BLUE_BOLD +"oooooooooooo ooooo ooooo      ooo       .o.       ooooo             oooooooooooo ooooooooo.   oooooooooooo ooooo      ooo  oooooooooooo oooooo   oooo \n" +
                ANSIColors.RED_BOLD +"`888'     `8 `888' `888b.     `8'      .888.      `888'             `888'     `8 `888   `Y88. `888'     `8 `888b.     `8' d'\"\"\"\"\"\"d888'  `888.   .8'  \n" +
                ANSIColors.BLUE_BOLD +" 888          888   8 `88b.    8      .8\"888.      888               888          888   .d88'  888          8 `88b.    8        .888P     `888. .8'   \n" +
                ANSIColors.RED_BOLD +" 888oooo8     888   8   `88b.  8     .8' `888.     888               888oooo8     888ooo88P'   888oooo8     8   `88b.  8       d888'       `888.8'    \n" +
                ANSIColors.BLUE_BOLD +" 888    \"     888   8     `88b.8    .88ooo8888.    888               888    \"     888`88b.     888    \"     8     `88b.8     .888P          `888'     \n" +
                ANSIColors.RED_BOLD +" 888          888   8       `888   .8'     `888.   888       o       888          888  `88b.   888       o  8       `888    d888'    .P      888      \n" +
                ANSIColors.BLUE_BOLD +"o888o        o888o o8o        `8  o88o     o8888o o888ooooood8      o888o        o888o  o888o o888ooooood8 o8o        `8  .8888888888P      o888o     \n" +
                "                                                                                                                                                      \n" +
                "                                                                                                                                                      \n" +
                "                                                                                                                                                      \n"+ ANSIColors.RESET);
    }

    /**
     * prints the winning cup
     * @param winner is name of player winner
     */
    public static void printCup(String winner) {
        WriterHelper.printlnColored(ANSIColors.BLACK_BOLD,  "\n\n" +
                                        "        ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ \n" +
                                        "        ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ \n" +
                                        "   ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ \n" +
                                        " ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ \n" +
                                        "¶¶¶¶      ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶       ¶¶¶¶ \n" +
                                        "¶¶¶       ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶        ¶¶¶ \n" +
                                        "¶¶        ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶        ¶¶¶ \n" +
                                        "¶¶¶    ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶      ¶¶¶ \n" +
                                        "¶¶¶    ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶    ¶¶¶¶ \n" +
                                        " ¶¶¶   ¶¶¶ ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ ¶¶¶    ¶¶¶ \n" +
                                        " ¶¶¶¶   ¶¶¶ ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ ¶¶¶¶  ¶¶¶¶ \n" +
                                        "   ¶¶¶¶  ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ ¶¶¶¶¶ \n" +
                                        "    ¶¶¶¶¶¶¶¶ ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ ¶¶¶¶¶¶¶¶¶ \n" +
                                        "      ¶¶¶¶¶¶  ¶¶¶¶¶¶¶¶¶¶¶¶¶¶   ¶¶¶¶¶¶ \n" +
                                        "               ¶¶¶¶¶¶¶¶¶¶¶¶ \n" +
                                        "                 ¶¶¶¶¶¶¶¶ \n" +
                                        "                   ¶¶¶¶ \n" +
                                        "                   ¶¶¶¶ \n" +
                                        "                   ¶¶¶¶ \n" +
                                        "                   ¶¶¶¶ \n" +
                                        "               ¶¶¶¶¶¶¶¶¶¶¶¶ \n" +
                                        "            ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ \n" +
                                        "            ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ \n" +
                                        "            ¶¶¶            ¶¶¶ \n" +
                                        "            ¶¶¶"+createWinnerCupLabel(winner)+"¶¶¶ \n" +
                                        "            ¶¶¶            ¶¶¶ \n" +
                                        "            ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ \n" +
                                        "            ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ \n" +
                                        "          ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ \n" +
                                        "         ¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶\n" +
                                        "\n\n");
    }

    /**
     * Prints colored a message without \n using ansi colors
     * @param color is color used
     * @param message is the message to print
     */
    public static void printColored(ANSIColors color, String message) {
        System.out.print(color);
        printOnConsole(message);
        System.out.print(ANSIColors.RESET);
    }

    /**
     * prints with tag colored
     * @param color is color used
     * @param tag is tag wanted
     * @param message is message to print
     */
    public static void printWithTagColored(ANSIColors color, String tag, String message) {
        printColored(color, "["+tag+"] "+message);
    }

    /**
     * prints an error message if server is not available
     */
    public static void printServerNotAvailableErrorMessage() {
        printAdrenalinaTitle();
        printColored(ANSIColors.RED,MESSAGE_SERVER_ERROR);
    }

    /**
     * Creates the winning cup label
     * @param winner is the winner player
     * @return a string that is the label.
     */
    private static String createWinnerCupLabel(String winner) {
        final int DIM = 12;
        int winnerLen = winner.length();
        int spaces = (DIM - winnerLen) / 2;
        String label = "            ";
        char[] labelArray = label.toCharArray();
        for(int i=0; i<winnerLen; i++) {
            labelArray[spaces+i] = winner.charAt(i);
        }
        return new String(labelArray);
    }

    /**
     * prints the final scoreboard
     * @param ranking is the final scoreboard
     */
    public static void printRanking(Map<String, Integer> ranking) {
        int i = 1;
        for(String player: ranking.keySet()){
            WriterHelper.printlnColored(ANSIColors.YELLOW_BOLD, i+". "+player+" -  "+ranking.get(player)+" punti");
            i++;
        }
    }

    /**
     * print some blank rows
     * @param numRows is number of white rows wanted
     */
    public static void printWhiteRows(int numRows) {
        for(int i=0; i<numRows; i++)
            printlnOnConsole("");
    }

    /**
     * prints command line infos to start client from command line
     */
    public static void printCommandsInfoTable() {
        String leftAlignFormat = "| %-8s | %-58s | %-13s | %-13s |%n";

        System.out.format("+----------+------------------------------------------------------------+---------------+---------------+%n");
        System.out.format("| Command  | Description                                                | Syntax        | Default       |%n");
        System.out.format("+----------+------------------------------------------------------------+---------------+---------------+%n");
        System.out.format(leftAlignFormat, "-ui", "Selects the specified user interface", "[cli|gui]", "-");
        System.out.format(leftAlignFormat, "-com", "Selects the communication protocol", "[soc|rmi]", "-");
        System.out.format(leftAlignFormat, "-ip", "Selects the server ip address", "[server ip]", "localhost");
        System.out.format(leftAlignFormat, "-sp", "Selects the server socket port", "[port]", "12345");
        System.out.format(leftAlignFormat, "-rp", "Selects the server rmi port", "[port]", "12333");
        System.out.format("+----------+------------------------------------------------------------+---------------+---------------+%n");
        System.out.format("| Example  | -ui cli -com soc -ip 127.0.0.1 -sp 12345 -rp 12333                                         |%n");
        System.out.format("+----------+------------------------------------------------------------+---------------+---------------+%n");
    }
}
