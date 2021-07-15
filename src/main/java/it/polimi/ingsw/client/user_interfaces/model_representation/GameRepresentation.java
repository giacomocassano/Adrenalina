package it.polimi.ingsw.client.user_interfaces.model_representation;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents the game and is used to give to Clients a representation about it.
 * implements Serializable because is sent trough socket.
 * There are only getters and setters methods and there is no game logic inside.
 * We can find here: other players' representation, map's representation, your player representation and the GameBoard one.
 */

public class GameRepresentation {

    private MyPlayerRepresentation myPlayerRepresentation;
    private List<PlayerRepresentation> otherPlayersRepresentations;
    private MapRepresentation mapRepresentation;
    private GameBoardRepresentation gameBoardRepresentation;

    public GameRepresentation() {
        otherPlayersRepresentations = new ArrayList<>();
        mapRepresentation = new MapRepresentation();
    }

    public GameRepresentation(MyPlayerRepresentation myPlayerRepresentation, List<PlayerRepresentation> otherPlayersRepresentations, MapRepresentation mapRepresentation, GameBoardRepresentation gameBoardRepresentation) {
        this.myPlayerRepresentation = myPlayerRepresentation;
        this.otherPlayersRepresentations = otherPlayersRepresentations;
        this.mapRepresentation = mapRepresentation;
        this.gameBoardRepresentation = gameBoardRepresentation;
    }

    public void update(List<PlayerRepresentation> players, MapRepresentation map) {
        this.otherPlayersRepresentations = players;
        this.mapRepresentation = map;
    }

    public void addPlayerRepresentation(PlayerRepresentation rap) {
        this.otherPlayersRepresentations.add(rap);
    }

    public void setOtherPlayersRepresentations(List<PlayerRepresentation> otherPlayersRepresentations) {
        this.otherPlayersRepresentations = otherPlayersRepresentations;
    }

    public void updateOtherPlayerRepresentation(PlayerRepresentation rap) {
        for(PlayerRepresentation r: otherPlayersRepresentations)
            if(r.getName().equals(rap.getName()))
                r = rap;
    }

    public PlayerRepresentation getOtherPlayerRapresentation(String player) {
        for(PlayerRepresentation rap: otherPlayersRepresentations)
            if(rap.getName().equals(player))
                return rap;
        return null;
    }
    public List<PlayerRepresentation> getOtherPlayerRapresentation(){
        return otherPlayersRepresentations;
    }
    public void setMapRepresentation(MapRepresentation mapRepresentation) {
        this.mapRepresentation = mapRepresentation;
    }

    public MapRepresentation getMapRepresentation() {
        return mapRepresentation;
    }

    public MyPlayerRepresentation getMyPlayerRepresentation() {
        return myPlayerRepresentation;
    }

    public void setMyPlayerRepresentation(MyPlayerRepresentation myPlayerRepresentation) {
        this.myPlayerRepresentation = myPlayerRepresentation;
    }

    public GameBoardRepresentation getGameBoardRepresentation() {
        return gameBoardRepresentation;
    }

    public void setGameBoardRepresentation(GameBoardRepresentation gameBoardRepresentation) {
        this.gameBoardRepresentation = gameBoardRepresentation;
    }
}
