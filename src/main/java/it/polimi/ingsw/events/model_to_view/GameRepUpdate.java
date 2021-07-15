package it.polimi.ingsw.events.model_to_view;

import it.polimi.ingsw.client.user_interfaces.model_representation.GameBoardRepresentation;
import it.polimi.ingsw.client.user_interfaces.model_representation.MapRepresentation;
import it.polimi.ingsw.client.user_interfaces.model_representation.MyPlayerRepresentation;
import it.polimi.ingsw.client.user_interfaces.model_representation.PlayerRepresentation;
import it.polimi.ingsw.events.visitors.ViewVisitor;

import java.io.Serializable;
import java.util.List;

/**
 * Game Representation Update event is an event sent from server to clients
 * to update them about the game.
 * because Clients must be updated about the model during a match.
 * it cointains a representation of enemies, each client has a different representation of enemies and
 * his own representation (about his character).
 * Also there is a gameBoard representation that contains some infos about the game
 * and a map representation that contains the map and the stacks.
 */
public class GameRepUpdate extends EventFromModel implements Serializable {

    public boolean gameStarting;
    private MyPlayerRepresentation myPlayerRep;
    private List<PlayerRepresentation> otherPlayersReps;
    private MapRepresentation mapRep;
    private GameBoardRepresentation gameBoardRepresentation;


    /**
     * This is the constructor.
     */
    public GameRepUpdate() {
        this.gameStarting = false;
        this.setUpdateEvent(true);
    }

    public MyPlayerRepresentation getMyPlayerRep() {
        return myPlayerRep;
    }

    public void setMyPlayerRep(MyPlayerRepresentation myPlayerRep) {
        this.myPlayerRep = myPlayerRep;
    }

    public List<PlayerRepresentation> getOtherPlayersReps() {
        return otherPlayersReps;
    }

    public void setOtherPlayersReps(List<PlayerRepresentation> otherPlayersReps) {
        this.otherPlayersReps = otherPlayersReps;
    }

    public MapRepresentation getMapRep() {
        return mapRep;
    }

    public void setMapRep(MapRepresentation mapRep) {
        this.mapRep = mapRep;
    }

    public GameBoardRepresentation getGameBoardRepresentation() {
        return gameBoardRepresentation;
    }

    public void setGameBoardRepresentation(GameBoardRepresentation gameBoardRepresentation) {
        this.gameBoardRepresentation = gameBoardRepresentation;
    }

    public void setGameStarting(boolean gameStarting) {
        this.gameStarting = gameStarting;
    }

    public boolean isGameStarting() {
        return gameStarting;
    }

    @Override
    public void acceptViewVisitor(ViewVisitor visitor) {
        visitor.visit(this);
    }
}
