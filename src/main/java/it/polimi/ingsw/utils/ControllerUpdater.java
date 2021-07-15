package it.polimi.ingsw.utils;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.events.client_to_server.to_controller.EventFromView;

/**
 * This is
 */
public class ControllerUpdater {

    private Controller controller;

    public void register(Controller controller){
        this.controller = controller;
    }

    public void notify(EventFromView eventFromView){
        controller.update(eventFromView);
    }
}
