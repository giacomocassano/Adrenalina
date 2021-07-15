package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.events.visitors.ViewVisitor;
import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This notification is sent by server to clients when game ends to notify them.
 */
public class GameOverNotification extends EventFromModel implements Serializable {

    private String winner;
    private Color winnerColor;
    private List<String> tiePlayers;
    private Map<String, Integer> ranking;

    /**
     *
     * @param player is client' character's name
     * @param winner is the player that has won the match
     * @param winnerColor is the color of the player that has won the match
     * @param tiePlayers are players with same score if game ends with parity
     * @param ranking is the final scoreboard
     */
    public GameOverNotification(String player, String winner, Color winnerColor, List<String> tiePlayers, Map<String, Integer> ranking) {
        super(player);
        this.winner = winner;
        this.tiePlayers = tiePlayers;
        this.winnerColor = winnerColor;
        this.ranking = ranking;
    }

    public String getWinner() {
        return winner;
    }

    public List<String> getTiePlayers() {
        return tiePlayers;
    }

    public Map<String, Integer> getRanking() {
        return ranking;
    }

    public Color getWinnerColor() {
        return winnerColor;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
