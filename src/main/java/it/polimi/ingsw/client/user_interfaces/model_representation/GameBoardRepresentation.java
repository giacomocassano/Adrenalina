package it.polimi.ingsw.client.user_interfaces.model_representation;

import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents the game board and is used to give to Clients a representation about it.
 * implements Serializable because is sent trough socket.
 * There are only getters and setters methods and there is no game logic inside.
 */
public class GameBoardRepresentation implements Serializable {
    private int empowerStackSize;
    private int weaponsStackSize;
    private int ammoStackSize;
    private List<Color> killshotTrackRep;

    /**
     * Size of empower stack getter
     * @return size of emp stack
     */
    public int getEmpowerStackSize() {
        return empowerStackSize;
    }

    /**
     * Size of empower stack setter
     * @param empowerStackSize is size of emp stack
     */
    public void setEmpowerStackSize(int empowerStackSize) {
        this.empowerStackSize = empowerStackSize;
    }

    /**
     * Size of weapon stack getter
     * @return size of weapon stack
     */
    public int getWeaponsStackSize() {
        return weaponsStackSize;
    }

    /**
     * Size of weapon stack setter
     * @param weaponsStackSize is size of weapon stack
     */
    public void setWeaponsStackSize(int weaponsStackSize) {
        this.weaponsStackSize = weaponsStackSize;
    }

    /**
     * Size of ammo stack getter
     * @return size of ammo stack
     */
    public int getAmmoStackSize() {
        return ammoStackSize;
    }

    /**
     * Ammunition stack size setter
     * @param ammoStackSize is size of ammo stack
     */
    public void setAmmoStackSize(int ammoStackSize) {
        this.ammoStackSize = ammoStackSize;
    }

    /**
     * killshotTrack getter
     * @return killshottrack representation
     */
    public List<Color> getKillshotTrackRep() {
        return killshotTrackRep;
    }

    /**
     * KillShotTrack setter
     * @param killshotTrackRep is the killShotTrack representation
     */
    public void setKillshotTrackRep(List<Color> killshotTrackRep) {
        this.killshotTrackRep = killshotTrackRep;
    }
}
