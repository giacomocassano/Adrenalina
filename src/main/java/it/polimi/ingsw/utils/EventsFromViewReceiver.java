package it.polimi.ingsw.utils;

import it.polimi.ingsw.events.client_to_server.to_controller.EventFromView;

/**
 * This is an Interface to filter events Server Side.
 */
public interface EventsFromViewReceiver {
    void receiveEvent(EventFromView event);
}
