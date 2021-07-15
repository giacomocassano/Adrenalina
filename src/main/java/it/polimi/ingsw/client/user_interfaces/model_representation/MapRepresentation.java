package it.polimi.ingsw.client.user_interfaces.model_representation;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.cards.AmmunitionCard;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Like other classes in this package, MapRepresentation contains a small representation about the map.
 * Contains weapon's in respawn point and ammunition in map
 * Implements Serializable because is sent through sockets.
 *
 */
public class MapRepresentation implements Serializable {
    private int mapType;
    private Map<Position, AmmunitionCard> ammosInPosition;
    private List<WeaponInfo> redWeapons;
    private List<WeaponInfo> blueWeapons;
    private List<WeaponInfo> yellowWeapons;

    public MapRepresentation() {
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public Map<Position, AmmunitionCard> getAmmosInPosition() {
        return ammosInPosition;
    }

    public void setAmmosInPosition(Map<Position, AmmunitionCard> ammosInPosition) {
        this.ammosInPosition = ammosInPosition;
    }

    public List<WeaponInfo> getRedWeapons() {
        return redWeapons;
    }

    public void setRedWeapons(List<WeaponInfo> redWeapons) {
        this.redWeapons = redWeapons;
    }

    public List<WeaponInfo> getBlueWeapons() {
        return blueWeapons;
    }

    public void setBlueWeapons(List<WeaponInfo> blueWeapons) {
        this.blueWeapons = blueWeapons;
    }

    public List<WeaponInfo> getYellowWeapons() {
        return yellowWeapons;
    }

    public void setYellowWeapons(List<WeaponInfo> yellowWeapons) {
        this.yellowWeapons = yellowWeapons;
    }
}
