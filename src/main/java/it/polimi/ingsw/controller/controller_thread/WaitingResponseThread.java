package it.polimi.ingsw.controller.controller_thread;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.client_to_server.to_controller.EventFromView;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.utils.ANSIColors;
import it.polimi.ingsw.utils.WriterHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * WaitingResponseThread rappresenta il thread che attende la ricezione degli eventi da parte dei client.
 * In particolare, quando il Controller riceve un evento rimuove dalla lista dei waitedPlayers il giocatore da cui ha
 * ricevuto l'evento.
 * Al termine dei waitingSeconds, se la lista è vuota allora il controller ha ricevuto tutti gli eventi di risposta,
 * altrimenti notifica i client con un evento di notifica TimeExceededNotification e manda al controller l'evento
 * defaultEvent, cioè un evento di default fornito al posto del client.
 */
public class WaitingResponseThread extends Thread {


    private final Controller controller;
    private List<Player> waitedPlayers;
    private EventFromView defaultEvent;
    private final int waitingSeconds;
    private boolean isWaiting;
    private long sleepingTimeMillis = 5;
    private ANSIColors color;

    /**
     * Constructor.
     * @param threadName is the name for thread.
     * @param controller is the controller.
     * @param waitedPlayers is the list of players to wait.
     * @param defaultEvent is the event to send when the time exceeds.
     * @param waitingSeconds are the timer seconds.
     */
    public WaitingResponseThread(String threadName, Controller controller, List<Player> waitedPlayers, EventFromView defaultEvent, int waitingSeconds) {
        this.setName(threadName);
        this.controller = controller;
        this.waitedPlayers = waitedPlayers;
        this.defaultEvent = defaultEvent;
        this.waitingSeconds = waitingSeconds;
        this.isWaiting = false;
        List<ANSIColors> colors = new ArrayList<>(Arrays.asList(ANSIColors.BLUE_BOLD, ANSIColors.RED_BOLD, ANSIColors.CYAN_BOLD, ANSIColors.YELLOW_BOLD, ANSIColors.GREEN_BOLD, ANSIColors.WHITE_BOLD, ANSIColors.MAGENTA_BOLD));
        this.color = colors.get(new Random().nextInt(colors.size()));
    }

    /**
     * Override of run method of Runnable class
     */
    @Override
    public void run() {
        //Initial sleep
        try {
            Thread.sleep(sleepingTimeMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(waitedPlayers.isEmpty()) {
            controller.update(defaultEvent);
        }else{
            WriterHelper.printWithTagColored(color, "THREAD", getName() + " partito.\n");
            long t = 0;
            long totalMillis = (long) waitingSeconds * 1000;
            int timer = waitingSeconds;
            WriterHelper.printWithTagColored(color, "THREAD", "Aspetto una risposta di ");
            for (Player p : waitedPlayers)
                WriterHelper.printColored(color, p.getName() + " ");
            WriterHelper.printColored(color," ancora per: " + timer);
            isWaiting = true;
            while (t < totalMillis && !waitedPlayers.isEmpty()) {
                try {
                    Thread.sleep(sleepingTimeMillis);
                } catch (InterruptedException e) {
                    WriterHelper.printErrorMessage("Errore nello sleep del thread.");
                }
                t += sleepingTimeMillis;

                //Print timer
                if (t % 1000 == 0) {
                    WriterHelper.printOnConsole("\b\b");
                    if (timer > 9)
                        WriterHelper.printColored(color, ""+timer);
                    else
                        WriterHelper.printColored(color, " " + timer);
                    timer--;
                }
            }
            //Now thread is not waiting
            isWaiting = false;
            //Notify time exceeded to the remaining waited players
            WriterHelper.printlnOnConsole("");
            if (!waitedPlayers.isEmpty()) {
                controller.responseTimeExceeded(waitedPlayers);
                WriterHelper.printWithTagColored(color, "THREAD", "Non tutti i giocatori hanno risposto al thread " +this.getName()+", notifico con l'evento di default.\n\n");
                controller.update(defaultEvent);
            }else{
                WriterHelper.printWithTagColored(color, "THREAD","Tutti i giocatori hanno risposto al thread "+this.getName()+"\n\n");
            }
        }
    }

    /**
     * Remove the player for waiting player list.
     * @param player is the player to remove.
     */
    public void removeWaitedPlayer(Player player) {
        this.waitedPlayers.remove(player);
    }

    /**
     * Getter.
     * @return true if the thread is still waiting.
     */
    public boolean isWaiting() {
        return isWaiting;
    }
}
