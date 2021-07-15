package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.utils.ControllerUpdater;
import it.polimi.ingsw.utils.ModelObserver;

/**
 * Class that represents View in the Model View Controller pattern.
 */
public abstract class View implements ModelObserver {

    private Player player;

    private ControllerUpdater controllerUpdater;

    /**
     * Constructor.
     * @param player is the corresponding view player.
     */
    public View(Player player) {
        this.player = player;
        this.controllerUpdater = new ControllerUpdater();
    }

    /**
     * Getter.
     * @return controller updater.
     */
    public ControllerUpdater getControllerUpdater() {
        return controllerUpdater;
    }

    /**
     * Getter.
     * @return the player.
     */
    @Override
    public Player getPlayer() {
        return player;
    }
}
