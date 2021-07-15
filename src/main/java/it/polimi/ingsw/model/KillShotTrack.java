package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class KillShotTrack {

    private int skulls;
    private List<Damage> markers;
    private List<Integer> indices;
    private int remaining;
    private boolean finalFrenzy;
    private Game game;
    private List<Integer> points;

    //defines.
    private static final int FIRST_DEATH_POINTS = 8;
    private static final int SECOND_DEATH_POINTS = 6;
    private static final int THIRD_DEATH_POINTS = 4;
    private static final int FOURTH_DEATH_POINTS = 2;
    private static final int LAST_DEATHS_POINTS = 1;

    private LinkedHashMap<Player, Integer> playerPoints;

    /**
     * Constructor
     * @param skulls is the num of skulls in the game.
     * @param game is the game that is about to be played
     */
    public KillShotTrack(int skulls, Game game) {
        this.skulls = skulls;
        this.markers = new ArrayList<>();
        this.indices = new ArrayList<>();
        this.remaining = skulls;
        this.finalFrenzy = false;
        this.game = game;
        this.playerPoints = new LinkedHashMap<>();
        addPoints();
    }

    /**
     * Add points to killShot Track
     */
    public void addPoints(){
        points=new ArrayList<>();
        points.add(FIRST_DEATH_POINTS );
        points.add(SECOND_DEATH_POINTS);
        points.add(THIRD_DEATH_POINTS);
        points.add(FOURTH_DEATH_POINTS);
        points.add(LAST_DEATHS_POINTS);
        points.add(LAST_DEATHS_POINTS);
    }

    public List<Damage> getMarkers() {
        return markers;
    }

    /**
     * Adds a kill in the killShotTrack
     *
     * @param killer is the death's killer
     * @param infert indicates if the death is an inferted death
     *
     */

    public void addKill(Player killer, boolean infert) {
        //Add kill marker and indices
        markers.add(new Damage(killer));
        indices.add(skulls - remaining + 1);
        if (infert) {
            markers.add(new Damage(killer));
            indices.add(skulls - remaining + 1);
        }
        //Decrease remaining if the game is not in final frenzy
        if(remaining > 0)
            remaining--;
        //Control if activate final frenzy
        if (remaining == 0) {
            finalFrenzy = true;
            game.startFinalFrenzy();
        }
    }


    public int getRemainingKills() {
        return remaining;
    }

    public boolean isFinalFrenzy() {
        return finalFrenzy;
    }

    /**
     *
     * @return a killShotTrack representation
     */

    public List<Color> getKillShotRep() {
        int i = 1;
        List<Color> colors = new ArrayList<>();
        for (int j = 0; j < markers.size(); j++) {
            if (indices.get(j) == i) {
                colors.add(markers.get(j).getShooter().getColor());
                i++;
            }
        }
        return colors;
    }

    /**
     * gives killShotTrack points to the players
     */

    public void addFinalPoints() {
        List<Player> players = new ArrayList<>(game.getPlayers());
        int max = 0;
        int tmp;
        Player bestPlayer = null;
        for (int i = 0; i < players.size(); i++) {
            for (Player player : players) {
                tmp = this.getNumMarkersOfPlayer(player);
                if (tmp >= max && tmp != 0) {
                    if (tmp == max)
                        bestPlayer = solveParity(tmp);
                    else {
                        max = tmp;
                        bestPlayer = player;
                    }
                }
            }
            if (max != 0 && bestPlayer != null) {
                bestPlayer.addPoints(this.points.remove(0));
                removeMarksOfPlayer(bestPlayer);
            }
            max = 0;
        }
    }


    /**
     * removes the player's marks in the killShotTrack
     * @param player is the player who has marks in the killShotTrack
     */
    private void removeMarksOfPlayer(Player player){
        int size=this.markers.size();
        for (int i=0;i<size;i++){
            if(this.markers.get(i).getShooter().equals(player)){
                this.markers.remove(i);
                i--;
                size--;
            }
        }
    }

    /**
     *
     * @param player is the player who has marks in the killShotTrack
     * @return  the number of marks of the player in the killShotTrack
     */
    private int getNumMarkersOfPlayer(Player player) {
        int cont = 0;
        for(Damage damage: this.markers)
            if(damage.getShooter() == player)
                cont++;
        return cont;
    }

    /**
     * @param num   is the number of players' marks that
     *              caused the parity instance
     *
     * @return player who has to get the points after solving the parity
     */
    private Player solveParity(int num) {
        int min = 5;
        int tmp;
        List<Player> parityPlayers = new ArrayList<>();
        Player bestPlayer = null;
        for (Damage damage : this.markers) {
            if (getNumMarkersOfPlayer(damage.getShooter()) == num&&!parityPlayers.contains(damage.getShooter()))
                parityPlayers.add(damage.getShooter());
        }
        for (Damage damage : this.markers) {
            if (parityPlayers.contains(damage.getShooter())) {
                tmp = this.markers.indexOf(damage);
                if (tmp < min) {
                    min = tmp;
                    bestPlayer = damage.getShooter();
                }
            }
        }
        return bestPlayer;
    }


    public int getKillShotTrackPlayerPoints(Player player) {
        if(playerPoints.containsKey(player))
            return playerPoints.get(player);
        return 0;
    }
}